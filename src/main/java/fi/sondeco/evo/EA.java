package fi.sondeco.evo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EA {

  public static int[] permutedIndices(int amount) {
    List<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < amount; i++)
      list.add(i);
    Collections.shuffle(list);
    
    int[] ret = new int[amount];
    for (int i = 0; i < amount; i++)
      ret[i] = list.get(i);
    
    return ret;
  }
  
}
