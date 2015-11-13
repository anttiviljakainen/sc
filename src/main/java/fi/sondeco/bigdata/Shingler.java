package fi.sondeco.bigdata;

import java.util.HashSet;
import java.util.Set;

/**
 * Shingler, generator of shingles.
 * 
 * @author Antti Viljakainen
 */
public class Shingler {

  /**
   * Generates k-shingles from given string. k is the length of
   * shingles produced.
   * 
   * General rule of thumb is roughly k=5 for emails etc short 
   * documents, k=9 for large documents, articles etc when used
   * for document similarity. Intuition is that low k results in 
   * greater similarity (shorter shingles are present in most 
   * documents).
   * 
   * @param string
   * @param k
   * @return
   */
  public static Set<String> kShingles(String string, int k) {
    Set<String> shingles = new HashSet<String>();
    
    for (int i = 0; i <= string.length() - k; i++) {
      String shingle = string.substring(i, i + k);
      
      // Java Set.add takes care of not adding duplicates
      shingles.add(shingle);
    }
    
    return shingles;
  }
}
