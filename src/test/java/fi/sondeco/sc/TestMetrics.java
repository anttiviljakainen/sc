package fi.sondeco.sc;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class TestMetrics {

  @Test
  public void testJaccardDistanceDiffSets() {
    Set<String> set1 = new HashSet<String>();
    Set<String> set2 = new HashSet<String>();
    
    set1.add("a");
    set1.add("b");
    set1.add("c");
    set2.add("d");
    set2.add("e");
    set2.add("f");
    
    assertEquals(1, Metrics.jaccardDistance(set1, set2), 0);
  }

  @Test
  public void testJaccardDistance1() {
    Set<String> set1 = new HashSet<String>();
    Set<String> set2 = new HashSet<String>();
    
    set1.add("a");
    set1.add("b");
    set1.add("c");
    set2.add("b");
    set2.add("c");
    set2.add("d");
    
    assertEquals(0.50, Metrics.jaccardDistance(set1, set2), 0);
  }

  
  @Test
  public void testJaccardDistance2() {
    Set<String> set1 = new HashSet<String>();
    Set<String> set2 = new HashSet<String>();
    
    set1.add("a");
    set1.add("b");
    set1.add("c");
    set2.add("c");
    set2.add("d");
    set2.add("e");
    
    assertEquals(0.80, Metrics.jaccardDistance(set1, set2), 0);
  }

  @Test
  public void testJaccardDistance3() {
    Set<String> set1 = new HashSet<String>();
    Set<String> set2 = new HashSet<String>();
    
    set1.add("b");
    set1.add("c");
    set2.add("c");
    set2.add("b");
    
    assertEquals(0, Metrics.jaccardDistance(set1, set2), 0);
  }

  @Test
  public void testCosineSimilarityEquality() {
    Map<String, Integer> map1 = new HashMap<String, Integer>();
    Map<String, Integer> map2 = new HashMap<String, Integer>();

    map1.put("a", 2);
    map1.put("b", 7);

    map2.put("a", 2);
    map2.put("b", 7);
    
    assertEquals(1, Metrics.cosineSimilarity(map1, map2), 0);
  }

  @Test
  public void testCosineSimilarityInequality() {
    Map<String, Integer> map1 = new HashMap<String, Integer>();
    Map<String, Integer> map2 = new HashMap<String, Integer>();

    map1.put("a", 2);
    map1.put("b", 7);

    map2.put("c", 2);
    map2.put("d", 7);
    
    assertEquals(0, Metrics.cosineSimilarity(map1, map2), 0);
  }

  @Test
  public void testCosineDistanceEquality() {
    Map<String, Integer> map1 = new HashMap<String, Integer>();
    Map<String, Integer> map2 = new HashMap<String, Integer>();

    map1.put("a", 2);
    map1.put("b", 7);

    map2.put("a", 2);
    map2.put("b", 7);
    
    assertEquals(0, Metrics.cosineDistance(map1, map2), 0);
  }

  @Test
  public void testCosineDistanceInequality() {
    Map<String, Integer> map1 = new HashMap<String, Integer>();
    Map<String, Integer> map2 = new HashMap<String, Integer>();

    map1.put("a", 2);
    map1.put("b", 7);

    map2.put("c", 2);
    map2.put("d", 7);
    
    assertEquals(Math.PI / 2, Metrics.cosineDistance(map1, map2), 0.001);
  }
  
  @Test
  public void testAngularDistanceEquality() {
    Map<String, Integer> map1 = new HashMap<String, Integer>();
    Map<String, Integer> map2 = new HashMap<String, Integer>();

    map1.put("a", 2);
    map1.put("b", 7);

    map2.put("a", 2);
    map2.put("b", 7);
    
    assertEquals(0, Metrics.angularDistance(map1, map2, true), 0);
  }

  @Test
  public void testAngularDistanceInequality() {
    Map<String, Integer> map1 = new HashMap<String, Integer>();
    Map<String, Integer> map2 = new HashMap<String, Integer>();

    map1.put("a", 2);
    map1.put("b", 7);

    map2.put("c", 2);
    map2.put("d", 7);
    
    assertEquals(1, Metrics.angularDistance(map1, map2, true), 0);
  }
  
}
