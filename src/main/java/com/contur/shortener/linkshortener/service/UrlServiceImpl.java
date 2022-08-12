package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.repository.UrlRepository;
import com.contur.shortener.linkshortener.util.UrlShortener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service serve as facade for all controllers.
 */

@Service
public class UrlServiceImpl implements UrlService {

  private final UrlRepository repository;
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

}
