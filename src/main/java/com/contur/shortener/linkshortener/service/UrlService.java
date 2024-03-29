package com.contur.shortener.linkshortener.service;

import com.contur.shortener.linkshortener.entity.Url;
import java.util.List;

/**
 * Implementation of this interface will be Service.
 */

public interface UrlService {

  Url generateAndSave(Url link);

  Url getOriginalUrl(String link);

  Url getUrlStats(String link);

  List<Url> getUrlsRaiting(int page, int count);

  Url putOnCache(Url url, String link);
}
