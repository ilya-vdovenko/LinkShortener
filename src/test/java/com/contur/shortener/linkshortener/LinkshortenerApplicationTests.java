package com.contur.shortener.linkshortener;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.contur.shortener.linkshortener.entity.Url;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class LinkshortenerApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Order(1)
  void getShortLinkTest() throws Exception {
    Url testUrl = new Url();
    testUrl.setId(1234567890753159L);
    testUrl.setOriginal("https://github.com/ilya-vdovenko/LinkShortener");
    this.mockMvc.perform(post("/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testUrl)))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.link").value("/l/fOJqhKY0x"));
  }

  @Test
  @Order(2)
  void getOriginalUrlTest() throws Exception {
    String testLink = "fOJqhKY0x";
    this.mockMvc.perform(get("/l/{link}", testLink))
        .andExpect(status().is3xxRedirection());
  }

}
