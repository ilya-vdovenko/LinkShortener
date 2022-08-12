package com.contur.shortener.linkshortener.repository;

import com.contur.shortener.linkshortener.entity.Url;

/**
 * Repository class for {@link Url} domain objects.
 */

public interface UrlRepository {

  Url save(Url link);

}