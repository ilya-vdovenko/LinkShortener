package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.repository.UrlRepository;
import com.contur.shortener.linkshortener.util.UrlShortener;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service serve as facade for all controllers.
 */

@Service
public class UrlServiceImpl implements UrlService {

  private final UrlRepository repository;
  private final int maxCountPerPage = 100;
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlServiceImpl.class);

  public UrlServiceImpl(UrlRepository rep) {
    this.repository = rep;
  }

  @Override
  public Url generateAndSave(Url url) {
    String shortLink = UrlShortener.generateUniqueUrl(url.getId());
    url.setLink(shortLink);
    LOGGER.info("Generate unique short link: {}", shortLink);
    return repository.save(url);
  }

  @Override
  public Url getOriginalUrl(String link, boolean update) {
    Long id = UrlShortener.getIdFromUniqueUrl(link);
    LOGGER.info("Get id: {} from short link {}", id, link);
    Url url = repository.findById(id);
    if (update) {
      url.increaseCount();
      repository.save(url);
      LOGGER.info("Update count stats for id: {}, shortlink:{}", id, link);
    }
    return url;
  }

  @Override
  public Url getUrlStats(String link) {
    updateRank();
    return getOriginalUrl(link, false);
  }

  @Override
  public List<Url> getUrlsRaiting(int page, int count) {
    updateRank();
    if (count > maxCountPerPage) {
      count = 100;
      LOGGER.info("Set count to {}", maxCountPerPage);
    }
    return repository.findAllByOrderByRankAsc(PageRequest.of(page, count));
  }

  private void updateRank() {
    List<Url> urls = repository.findAllByOrderByCountDesc();
    AtomicInteger rank = new AtomicInteger(0);
    urls.stream().forEach(url -> url.setRank(rank.incrementAndGet()));
    repository.saveAll(urls);
    LOGGER.info("Update rank stats for links");
  }

}
