package com.contur.shortener.linkshortener.controller;

import com.contur.shortener.linkshortener.entity.Link;
import com.contur.shortener.linkshortener.service.LinkService;
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
class LinkController {

  private final LinkService service;
  private static final Logger LOGGER = LoggerFactory.getLogger(LinkController.class);

  public LinkController(LinkService ls) {
    this.service = ls;
  }

  @PostMapping
  Link getLink(@RequestBody Link link) {
    LOGGER.info("Received url to shorten: " + link.getOriginal());
    return service.generateAndSave(link);
  }

  @GetMapping(value = "/l/{link}")
  void redirectToOriginal(@PathVariable String link,
      HttpServletResponse response) throws IOException {
    response.sendRedirect("original url");
  }

}
