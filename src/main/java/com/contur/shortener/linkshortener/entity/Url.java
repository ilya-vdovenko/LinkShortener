package com.contur.shortener.linkshortener.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class for storage original and new link.
 **/

@Entity
@Getter
@Setter
@ToString
@Table(name = "links")
@JsonInclude(Include.NON_NULL)
public class Url {

  @Id
  private long id;

  @Column
  private String original;

  @Column
  private String link;

  @Column
  private int rank;

  @Column
  private long count;

  {
    Random randomId = new Random(System.currentTimeMillis());
    id = randomId.nextLong();
    if (id < 0) {
      id = id * (-1);
    }
  }

}
