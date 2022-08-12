package com.contur.shortener.linkshortener.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class for storage original and new link.
 **/

@Entity
@Table(name = "links")
@JsonInclude(Include.NON_NULL)
public class Url {

  @Id
  private long id;

  @Column(name = "orignal")
  private String original;

  @Column(name = "link")
  private String link;

  {
    Random randomId = new Random(System.currentTimeMillis());
    id = randomId.nextLong();
    if (id < 0) {
      id = id * (-1);
    }
  }

  public void setId(long id) {
    this.id = id;
    System.out.println("set id " + this.id);
  }

  public long getId() {
    System.out.println("get id " + this.id);
    return this.id;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getOriginal() {
    return original;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLink() {
    return link;
  }

}
