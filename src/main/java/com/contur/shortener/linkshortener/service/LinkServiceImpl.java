package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Link;
import com.contur.shortener.linkshortener.repository.LinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service serve as facade for all controllers.
 */

@Service
public class LinkServiceImpl implements LinkService {

  private final LinkRepository repository;
  private static final Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);

  public LinkServiceImpl(LinkRepository rep) {
    this.repository = rep;
  }

  @Override
  public Link generateAndSave(Link link) {
    LOGGER.info("Shortening: {}", link.getOriginal());
    // TODO implements of link shortening, and save in Link
    link.setLink("shortLink");
    return repository.save(link);
  }

}
