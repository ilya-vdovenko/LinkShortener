package com.contur.shortener.linkshortener.repository;

import com.contur.shortener.linkshortener.entity.Url;
import org.springframework.data.repository.Repository;

/**
 * Spring Data jpa specialization of the {@link UrlRepository} interface.
 */

public interface SpringDataUrlRepository extends UrlRepository, Repository<Url, Integer> {

}
