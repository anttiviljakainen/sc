package fi.sondeco.sc;

import java.util.Set;

/**
 * Metrics for various computing needs.
 * 
 * @author Antti Viljakainen
 */
public class Metrics {

  /**
   * Returns Jaccard similarity between two sets. Output will be
   * closer to 1 for similar sets and 0 for dissimilar sets.
   * 
   * Jaccard similarity is calculated by taking the intersection 
   * of the sets divided by the union of the sets.
   * 
   * @param set1 first set
   * @param set2 second set
   * @return jaccard similarity
   */
  public static double jaccardSimilarity(Set<?> set1, Set<?> set2) {
    int intersection = 0;
    int union = set1.size() + set2.size();
    
    for (Object o : set1) {
      if (set2.contains(o)) {
        union--;
        intersection++;
      }
    }

    return union != 0 ? (double) intersection / union : 0;
  }
  
  /**
   * Returns Jaccard distance of two sets. Jaccard distance
   * is 1 - jaccardSimilarity(S1, S2) so distance will be 0
   * for similar sets and 1 for dissimilar sets.
   * 
   * @param set1 first set
   * @param set2 second set
   * @return jaccard distance
   */
  public static double jaccardDistance(Set<?> set1, Set<?> set2) {
    return 1 - jaccardSimilarity(set1, set2);
  }
}
