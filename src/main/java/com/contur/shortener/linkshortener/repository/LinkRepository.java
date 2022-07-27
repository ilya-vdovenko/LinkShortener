package com.contur.shortener.linkshortener.repository;

import com.contur.shortener.linkshortener.entity.Link;

/**
 * Repository class for {@link Link} domain objects.
 */

public interface LinkRepository {

  Link save(Link link);

}