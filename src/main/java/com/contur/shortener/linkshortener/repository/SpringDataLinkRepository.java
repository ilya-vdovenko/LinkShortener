package com.contur.shortener.linkshortener.repository;

import com.contur.shortener.linkshortener.entity.Link;
import org.springframework.data.repository.Repository;

/**
 * Spring Data jpa specialization of the {@link LinkRepository} interface.
 */

public interface SpringDataLinkRepository extends LinkRepository, Repository<Link, Integer> {

}
