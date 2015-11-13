package fi.sondeco.sc;

import static org.junit.Assert.*;

import java.util.HashSet;
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
  
}
