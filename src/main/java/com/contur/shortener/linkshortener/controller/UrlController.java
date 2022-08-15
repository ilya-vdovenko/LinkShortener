package com.contur.shortener.linkshortener.controller;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.service.UrlService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UrlController {

  private final UrlService service;
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlController.class);

  public UrlController(UrlService ls) {
    this.service = ls;
  }

  @PostMapping
  ShortLink getLink(@RequestBody Url url) {
    LOGGER.info("Received url to shorten: {}", url.getOriginal());
    ShortLink shortLink = new ShortLink();
    shortLink.setLink("/l/" + service.generateAndSave(url).getLink());
    return shortLink;
  }

  @GetMapping(value = "/l/{link}")
  void redirectToOriginal(@PathVariable String link,
      HttpServletResponse response) throws IOException {
    LOGGER.info("Received shortened url: {}", link);
    String originalLink = service.getOriginalUrl(link).getOriginal();
    LOGGER.info("Redirect to original url: {}", originalLink);
    response.sendRedirect(originalLink);
  }

  class ShortLink {

    private String link;

    public String getLink() {
      return link;
    }

    public void setLink(String url) {
      this.link = url;
    }
  }

}
