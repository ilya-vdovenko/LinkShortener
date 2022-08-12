package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Url;

/**
 * Implementation of this interface will be Service.
 */

public interface UrlService {

  Url generateAndSave(Url link);

}
