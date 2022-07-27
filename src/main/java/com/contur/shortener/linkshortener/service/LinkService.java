package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Link;

/**
 * Implementation of this interface will be Service.
 */

public interface LinkService {

  Link generateAndSave(Link link);

}
