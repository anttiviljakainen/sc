package fi.sondeco.sc;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRandomizer {
 
  @Test
  public void testUniqueInts() {
    int size = 55;
    int[] ui = Randomizer.uniqueInts(100, size);

    assertEquals(size, ui.length);
    
    for (int i = 0; i < ui.length - 1; i++) {
      for (int j = i + 1; j < ui.length; j++) {
        if (ui[i] == ui[j])
          fail(String.format("Integer %d has a duplicate.", ui[i]));
      }
    }
  }
}
