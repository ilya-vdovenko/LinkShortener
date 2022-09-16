package com.contur.shortener.linkshortener.repository;

import com.contur.shortener.linkshortener.entity.Url;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * Repository class for {@link Url} domain objects.
 */

public interface UrlRepository {

  Url save(Url link);

  List<Url> saveAll(Iterable<Object> urls);

  Url findById(long id);

  List<Object> findAllByOrderByCountDesc();

  List<Url> findAllByOrderByRankAsc(Pageable pageable);

}