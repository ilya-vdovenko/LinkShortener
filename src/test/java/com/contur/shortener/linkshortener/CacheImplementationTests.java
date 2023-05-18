package com.contur.shortener.linkshortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.contur.shortener.linkshortener.entity.Url;
import com.contur.shortener.linkshortener.repository.SpringDataUrlRepository;
import com.contur.shortener.linkshortener.service.UrlService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * Cache implemetation tests.
 */

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(CacheTestConfig.class)
@TestMethodOrder(OrderAnnotation.class)
public class CacheImplementationTests {

  @Autowired
  private UrlService service;

  @Autowired
  private SpringDataUrlRepository mockRepository;

  private final long id = 1234567890753159L;
  private final String testPathLink = "fOJqhKY0x";

  @BeforeAll
  void setup() {
    Url testUrl = new Url();
    testUrl.setId(id);
    testUrl.setOriginal("https://github.com/ilya-vdovenko/LinkShortener");
    given(mockRepository.findById(id)).willReturn(Optional.of(testUrl));
  }

  @RepeatedTest(2)
  @Order(1)
  void shouldFindOriginalUrlbyShortLink() throws Exception {
    Url url = this.service.getOriginalUrl(testPathLink);
    assertEquals(url.getOriginal(), "https://github.com/ilya-vdovenko/LinkShortener");
    verify(mockRepository, times(1)).findById(id);
  }

  @RepeatedTest(2)
  @Order(2)
  void shouldNotUseCache() throws Exception {
    Url url = this.service.getUrlStats(testPathLink);
    assertEquals(url.getOriginal(), "https://github.com/ilya-vdovenko/LinkShortener");
    verify(mockRepository, atMost(3)).findById(id);
  }

}
