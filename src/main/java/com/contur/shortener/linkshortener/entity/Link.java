package com.contur.shortener.linkshortener.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class for storage original and new link.
 **/

@Entity
@Table(name = "links")
public class Link {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "orignal")
  private String original;

  @Column(name = "link")
  private String link;

  public void setOriginal(String original) {
    this.original = original;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLink() {
    return link;
  }

  // TODO set only for needed parameter
  public String getOriginal() {
    return original;
  }
}
