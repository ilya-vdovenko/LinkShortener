package com.contur.shortener.linkshortener.controller;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.service.UrlService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
class UrlController {

  private final String redirectPath = "/l/";
  private final UrlService service;

  public UrlController(UrlService ls) {
    this.service = ls;
  }

  @PostMapping(produces = "application/json")
  String getLink(@RequestBody @Valid Url url)
      throws JsonProcessingException {
    log.info("Received url to shorten: {}", url.getOriginal());
    url.setLink(redirectPath + service.generateAndSave(url).getLink());
    return urlFilter(url, SimpleBeanPropertyFilter.filterOutAllExcept("link"));
  }

  @GetMapping(value = redirectPath + "{link}")
  void redirectToOriginal(@PathVariable(required = true) String link,
      HttpServletResponse response) throws IOException {
    log.info("Received shortened url: {}", link);
    Url url = service.putOnCache(service.getOriginalUrl(link), link);
    String originalLink = url.getOriginal();
    log.info("Redirect to original url: {}", originalLink);
    response.sendRedirect(originalLink);
  }

  @GetMapping(value = "/stats/{link}", produces = "application/json")
  String getStatsBylink(@PathVariable String link)
      throws JsonProcessingException {
    log.info("Received shortened url: {}", link);
    Url url = service.getUrlStats(link);
    url.setLink(redirectPath + url.getLink());
    return urlFilter(url, SimpleBeanPropertyFilter.serializeAllExcept("id"));
  }

  @GetMapping(value = "/stats", produces = "application/json")
  String getLinksRaiting(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int count) throws JsonProcessingException {
    log.info("Request links raiting with page:{}; count:{}", page, count);
    List<Url> urls = service.getUrlsRaiting(page, count);
    urls.forEach(url -> url.setLink(redirectPath + url.getLink()));
    log.info("Get requested links {}", urls);
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

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  String handleConstraintViolationException(ConstraintViolationException ex) {
    log.info("Constraint violation exception encountered: {}", ex.getConstraintViolations());
    return ex.getMessage();
  }
}