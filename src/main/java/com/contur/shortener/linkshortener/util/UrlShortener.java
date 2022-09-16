package com.contur.shortener.linkshortener.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Class for shortening and get back url.
 **/
@Slf4j
public class UrlShortener {

  private static List<Character> indexToCharTable;
  private static HashMap<Character, Integer> charToIndexTable;

  static {
    initCharIndexTables();
  }

  private static void initCharIndexTables() {
    indexToCharTable = new ArrayList<>();
    charToIndexTable = new HashMap<>();
    for (int i = 0; i < 26; i++) {
      char c = 'a';
      c += i;
      indexToCharTable.add(c);
      charToIndexTable.put(c, i);
    }
    for (int i = 26; i < 52; i++) {
      char c = 'A';
      c += (i - 26);
      indexToCharTable.add(c);
      charToIndexTable.put(c, i);
    }
    for (int i = 52; i < 62; i++) {
      char c = '0';
      c += (i - 52);
      indexToCharTable.add(c);
      charToIndexTable.put(c, i);
    }
  }

  /**
   * Generate unique url by given id.
   *
   * @param id id of original link.
   * @return unique link.
   */
  public static String generateUniqueUrl(long id) {
    log.debug("ID original === {}", id);
    List<Integer> base62digits = convertBase10ToBase62(id);
    StringBuilder url = new StringBuilder();
    for (int digit : base62digits) {
      url.append(indexToCharTable.get(digit));
      log.debug("char: {} - {}", indexToCharTable.get(digit), digit);
    }
    return url.toString();
  }

  private static List<Integer> convertBase10ToBase62(long id) {
    LinkedList<Integer> digits = new LinkedList<>();
    while (id > 0) {
      int remainder = (int) (id % 62);
      digits.addFirst(remainder);
      id /= 62;
      log.debug("reminder {}, id after del {}", remainder, id);
    }
    return digits;
  }

  /**
   * Get id from unique link.
   *
   * @param url unique link.
   * @return id of original link.
   */
  public static Long getIdFromUniqueUrl(String url) {
    log.debug("Short link: {}", url);
    List<Character> base62Number = new ArrayList<>();
    for (int i = 0; i < url.length(); i++) {
      base62Number.add(url.charAt(i));
    }
    log.debug("base62Number: {}", base62Number);
    return convertBase62ToBase10(base62Number);
  }

  private static Long convertBase62ToBase10(List<Character> nums) {
    long id = 0L;
    int exp = nums.size() - 1;
    for (int i = 0; i < nums.size(); i++, exp--) {
      int base10num = charToIndexTable.get(nums.get(i));
      log.debug("base10num: {} - {}", base10num, nums.get(i));
      id += (base10num * (long) Math.pow(62.0, exp));
      log.debug("id: {}", id);
    }
    log.debug("ID of original link: {}", id);
    return id;
  }

}
