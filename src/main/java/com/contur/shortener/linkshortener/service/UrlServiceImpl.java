package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.repository.UrlRepository;
import com.contur.shortener.linkshortener.util.UrlShortener;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service serve as facade for all controllers.
 */

@Slf4j
@Service
public class UrlServiceImpl implements UrlService {

  private final UrlRepository repository;
  private final CacheManager cacheManager;
  private final int maxCountPerPage = 100;

  public UrlServiceImpl(UrlRepository rep,
      CacheManager cache) {
    this.repository = rep;
    this.cacheManager = cache;
  }

  @Override
  public Url generateAndSave(Url url) {
    String shortLink = UrlShortener.generateUniqueUrl(url.getId());
    url.setLink(shortLink);
    log.info("Generate unique short link: {}", shortLink);
    return repository.save(url);
  }

  @Override
  @Cacheable(value = "Urls", key = "#link")
  public Url getOriginalUrl(String link) {
    Long id = UrlShortener.getIdFromUniqueUrl(link);
    log.info("Get id: {} from short link {}", id, link);
    Url url = repository.findById(id);
    if (url == null) {
      throw new ConstraintViolationException("Not found original link by short link " + link, null);
    }
    return url;
  }

  @Override
  @CachePut(value = "Urls", key = "#link")
  public Url putOnCache(Url url, String link) {
    url.increaseCount();
    log.info("Update count stats for id: {}, shortlink:{}", url.getId(), link);
    return url;
  }

  @Override
  public Url getUrlStats(String link) {
    updateRank();
    return getOriginalUrl(link);
  }

  @Override
  public List<Url> getUrlsRaiting(int page, int count) {
    updateRank();
    if (count > maxCountPerPage) {
      count = 100;
      log.info("Set count to {}", maxCountPerPage);
    }
    return repository.findAllByOrderByRankAsc(PageRequest.of(page, count));
  }

  private void updateRank() {
    ConcurrentMapCache cache = (ConcurrentMapCache) cacheManager.getCache("Urls");
    Iterable<Object> cacheUrls = cache.getNativeCache().values()
        .stream().collect(Collectors.toList());
    repository.saveAll(cacheUrls);
    List<Object> urls = repository.findAllByOrderByCountDesc();
    AtomicInteger rank = new AtomicInteger(0);
    urls.stream().forEach(url -> ((Url) url).setRank(rank.incrementAndGet()));
    repository.saveAll(urls);
    log.info("Update rank stats for links");
  }

}
