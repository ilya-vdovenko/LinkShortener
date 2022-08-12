package com.contur.shortener.linkshortener.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 **/
public class UrlShortener {

  private static List<Character> indexToCharTable;
  private static final Logger LOGGER = LoggerFactory.getLogger(UrlShortener.class);

  static {
    initIndexToCharTable();
  }

  private static void initIndexToCharTable() {
    indexToCharTable = new ArrayList<>();
    for (int i = 0; i < 26; i++) {
      char c = 'a';
      c += i;
      indexToCharTable.add(c);
    }
    for (int i = 26; i < 52; i++) {
      char c = 'A';
      c += (i - 26);
      indexToCharTable.add(c);
    }
    for (int i = 52; i < 62; i++) {
      char c = '0';
      c += (i - 52);
      indexToCharTable.add(c);
    }
  }

  /**
   * Generate unique url by given id.
   *
   * @param id id of original link.
   * @return unique link.
   */
  public static String generateUniqueUrl(long id) {
    LOGGER.debug("ID original === {}", id);
    List<Integer> base62digits = convertBase10ToBase62(id);
    StringBuilder url = new StringBuilder();
    for (int digit : base62digits) {
      url.append(indexToCharTable.get(digit));
      LOGGER.debug("char: {} - {}", indexToCharTable.get(digit), digit);
    }
    return url.toString();
  }

  private static List<Integer> convertBase10ToBase62(long id) {
    LinkedList<Integer> digits = new LinkedList<>();
    while (id > 0) {
      int remainder = (int) (id % 62);
      digits.addFirst(remainder);
      id /= 62;
      LOGGER.debug("reminder {}, id after del {}", remainder, id);
    }
    return digits;
  }

  /**
   * Get original url from unique link.
   *
   * @param url unique link.
   * @return id of original link.
   */
  public static Long getIdFromUniqueUrl(String url) {
    List<Character> base62Number = new ArrayList<>();
    for (int i = 0; i < url.length(); i++) {
      base62Number.add(url.charAt(i));
    }
    return convertBase62ToBase10(base62Number);
  }

  private static Long convertBase62ToBase10(List<Character> nums) {
    long id = 0L;
    int exp = nums.size() - 1;
    for (int i = 0; i < nums.size(); i++, exp--) {
      int base10num = indexToCharTable.get(nums.get(i));
      id += (base10num * Math.pow(62.0, exp));
    }
    return id;
  }

}
