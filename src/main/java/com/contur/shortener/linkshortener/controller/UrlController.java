package com.contur.shortener.linkshortener.controller;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.service.UrlService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UrlController {

  private final String redirectPath = "/l/";
  private final UrlService service;
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlController.class);

  public UrlController(UrlService ls) {
    this.service = ls;
  }

  @PostMapping(produces = "application/json")
  String getLink(@RequestBody Url url)
      throws JsonProcessingException {
    LOGGER.info("Received url to shorten: {}", url.getOriginal());
    url.setLink(redirectPath + service.generateAndSave(url).getLink());
    return urlFilter(url, SimpleBeanPropertyFilter.filterOutAllExcept("link"));
  }

  @GetMapping(value = redirectPath + "{link}")
  void redirectToOriginal(@PathVariable String link,
      HttpServletResponse response) throws IOException {
    LOGGER.info("Received shortened url: {}", link);
    String originalLink = service.getOriginalUrl(link, true).getOriginal();
    LOGGER.info("Redirect to original url: {}", originalLink);
    response.sendRedirect(originalLink);
  }

  @GetMapping(value = "/stats/{link}", produces = "application/json")
  String getStatsBylink(@PathVariable String link)
      throws JsonProcessingException {
    LOGGER.info("Received shortened url: {}", link);
    Url url = service.getUrlStats(link);
    url.setLink(redirectPath + url.getLink());
    return urlFilter(url, SimpleBeanPropertyFilter.serializeAllExcept("id"));
  }

  @GetMapping(value = "/stats", produces = "application/json")
  String getLinksRaiting(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int count) throws JsonProcessingException {
    LOGGER.info("Request links raiting with page:{}; count:{}", page, count);
    List<Url> urls = service.getUrlsRaiting(page, count);
    urls.forEach(url -> url.setLink(redirectPath + url.getLink()));
    LOGGER.info("Get requested links {}", urls);
    return urlFilter(urls, SimpleBeanPropertyFilter.serializeAllExcept("id"));
  }

  private <T> String urlFilter(T url, SimpleBeanPropertyFilter filter)
      throws JsonProcessingException {
    return jsonSettings(filter).writeValueAsString(url);
  }

  private ObjectWriter jsonSettings(SimpleBeanPropertyFilter filter) {
    SimpleFilterProvider filterProvider = new SimpleFilterProvider()
        .addFilter("urlFilter", filter);
    return new ObjectMapper()
        .setFilterProvider(filterProvider)
        .writerWithDefaultPrettyPrinter();
  }

}