package fi.sondeco.bigdata;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TestShingler {

  private static final String TEST_STRING = "Quick brown fox";
  
  @Test
  public void testShinglerK6() {
    Set<String> shingles = Shingler.kShingles(TEST_STRING, 6);

    assertEquals(10, shingles.size());
    
    assertTrue(shingles.contains("Quick "));
    assertTrue(shingles.contains("uick b"));
    assertTrue(shingles.contains("ick br"));
    assertTrue(shingles.contains("ck bro"));
    assertTrue(shingles.contains("k brow"));
    assertTrue(shingles.contains(" brown"));
    assertTrue(shingles.contains("brown "));
    assertTrue(shingles.contains("rown f"));
    assertTrue(shingles.contains("own fo"));
    assertTrue(shingles.contains("wn fox"));
  }

  @Test
  public void testShinglerK2() {
    Set<String> shingles = Shingler.kShingles("abcdabd", 2);

    assertEquals(5, shingles.size());

    assertTrue(shingles.contains("ab"));
    assertTrue(shingles.contains("bc"));
    assertTrue(shingles.contains("cd"));
    assertTrue(shingles.contains("da"));
    assertTrue(shingles.contains("bd"));
  }

  @Test
  public void testShinglerK5() {
    Set<String> shingles = Shingler.kShingles("abcd efg", 5);
    
    assertEquals(4, shingles.size());

    assertTrue(shingles.contains("abcd "));
    assertTrue(shingles.contains("bcd e"));
    assertTrue(shingles.contains("cd ef"));
    assertTrue(shingles.contains("d efg"));
  }
  
}
