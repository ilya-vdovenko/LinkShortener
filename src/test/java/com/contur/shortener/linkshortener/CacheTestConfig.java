package com.contur.shortener.linkshortener;

import com.contur.shortener.linkshortener.repository.UrlRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration for cache tests.
 */

@TestConfiguration
@EnableCaching
@AutoConfigureCache
@ComponentScan("com.contur.shortener.linkshortener.service")
public class CacheTestConfig {

  @Bean
  @Primary
  public UrlRepository mockUrlRepository() {
    return Mockito.mock(UrlRepository.class);
  }

}