package fi.sondeco.sc;

import java.util.Map;
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
  
  /**
   * Returns the cosine similarity of two maps where the keys are expected to be
   * from same set S and values representing the count of occurences of the key.
   * 
   * @param map1 map 1
   * @param map2 map 2
   * @return cosine similarity of the two maps
   */
  public static double cosineSimilarity(Map<?, Integer> map1, Map<?, Integer> map2) {
    double dot = 0;
    
    // Dot product
    for (Object key : map1.keySet()) {
      Integer i2 = map2.get(key);
      
      if (i2 != null)
        dot += map1.get(key) * i2;
    }
    
    // Magnitudes
    
    int mag1 = 0;
    int mag2 = 0;
    
    for (Integer value : map1.values())
      mag1 += value * value;

    for (Integer value : map2.values())
      mag2 += value * value;
    
    double divisor = Math.sqrt(mag1) * Math.sqrt(mag2);
    
    if (divisor != 0)
      return Math.min(dot / divisor, 1);
    else
      return 0;
  }

  /**
   * Returns cosine distance between two maps. For more information see 
   * cosineSimilarity for maps.
   * 
   * @param map1 map 1
   * @param map2 map 2
   * @return cosine distance of the two maps
   */
  public static double cosineDistance(Map<?, Integer> map1, Map<?, Integer> map2) {
    return Math.acos(cosineSimilarity(map1, map2));
  }
  
  /**
   * Returns angular distance between two maps. For more information see 
   * cosineSimilarity for maps.
   * 
   * If the vectors will have only positive coefficients, set the positiveCoefficients
   * parameter to true to maintain [0, 1] distance. If vectors include also negative
   * coefficients then set the parameter to false.
   * 
   * @param map1 map 1
   * @param map2 map 2
   * @param positiveCoefficients true if you only have positive coefficients on the vectors, false otherwise
   * @return cosine distance of the two maps
   */
  public static double angularDistance(Map<?, Integer> map1, Map<?, Integer> map2, boolean positiveCoefficients) {
    double m = positiveCoefficients ? 2 : 1;
    return m * Math.acos(cosineSimilarity(map1, map2)) / Math.PI;
  }
  
}
