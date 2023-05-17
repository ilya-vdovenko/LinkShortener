package com.contur.shortener.linkshortener.repository;

import com.contur.shortener.linkshortener.entity.Url;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data jpa specialization of the {@link UrlRepository} interface.
 */

public interface SpringDataUrlRepository extends PagingAndSortingRepository<Url, Long> {

        List<Url> findAllByOrderByCountDesc();

        List<Url> findAllByOrderByRankAsc(Pageable pageable);
}
