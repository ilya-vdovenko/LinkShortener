package com.contur.shortener.linkshortener;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.contur.shortener.linkshortener.entity.Url;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class LinkshortenerApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final String testPathLink = "fOJqhKY0x";
  private final SimpleFilterProvider filterProvider = new SimpleFilterProvider()
      .addFilter("urlFilter", SimpleBeanPropertyFilter.serializeAll());

  @Test
  @Order(1)
  void getShortLinkTest() throws Exception {
    Url testUrl = new Url();
    testUrl.setId(1234567890753159L);
    testUrl.setOriginal("https://github.com/ilya-vdovenko/LinkShortener");
    this.mockMvc.perform(post("/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.setFilterProvider(filterProvider).writeValueAsString(testUrl)))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.link").value("/l/" + testPathLink))
        .andExpect(jsonPath("$.id").doesNotExist());
  }

  @Test
  @Order(2)
  void getOriginalUrlTest() throws Exception {
    this.mockMvc.perform(get("/l/{link}", testPathLink))
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @Order(3)
  void getLinkStatsTest() throws Exception {
    this.mockMvc.perform(get("/stats/{link}", testPathLink))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.original").value("https://github.com/ilya-vdovenko/LinkShortener"))
        .andExpect(jsonPath("$.link").value("/l/" + testPathLink))
        .andExpect(jsonPath("$.rank").value("1"))
        .andExpect(jsonPath("$.count").value("1"))
        .andExpect(jsonPath("$.id").doesNotExist());
  }

  @Test
  @Order(4)
  void getLinksRaitingTest() throws Exception {
    Url testUrl2 = new Url();
    testUrl2.setId(1139567792753659L);
    testUrl2.setOriginal("https://github.com/ilya-vdovenko/LinkShortener?testlinks=true");
    MvcResult result = this.mockMvc.perform(post("/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.setFilterProvider(filterProvider).writeValueAsString(testUrl2)))
        .andReturn();
    String testlink = JsonPath.read(result.getResponse().getContentAsString(), "$.link");
    int times = 3;
    while (times > 0) {
      this.mockMvc.perform(get(testlink));
      times--;
    }
    this.mockMvc.perform(get("/stats")
        .param("page", "0")
        .param("count", "2"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.*").isArray())
        .andExpect(jsonPath("$.*", hasSize(2)))
        .andExpect(jsonPath("$.[0].original").value("https://github.com/ilya-vdovenko/LinkShortener?testlinks=true"))
        .andExpect(jsonPath("$.[0].link").value(testlink))
        .andExpect(jsonPath("$.[0].rank").value("1"))
        .andExpect(jsonPath("$.[0].count").value("3"))
        .andExpect(jsonPath("$.[1].original").value("https://github.com/ilya-vdovenko/LinkShortener"))
        .andExpect(jsonPath("$.[1].link").value("/l/" + testPathLink))
        .andExpect(jsonPath("$.[1].rank").value("2"))
        .andExpect(jsonPath("$.[1].count").value("1"));

  }
}
