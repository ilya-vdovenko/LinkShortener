package com.contur.shortener.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Entry point pf spring boot.
 */

@SpringBootApplication
@EnableCaching
public class LinkshortenerApplication {

  public static void main(String[] args) {
    SpringApplication.run(LinkshortenerApplication.class, args);
  }

}
