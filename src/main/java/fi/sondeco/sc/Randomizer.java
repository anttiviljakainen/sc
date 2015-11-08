package fi.sondeco.sc;

import java.util.Random;

public class Randomizer {

  /**
   * Randomizes count integers which are unique from eachother. 
   * Integers are between 0 and max (exclusive). Implemented by 
   * checking the previous integers in the list so shuffling method
   * may be faster if you need to select high number of items 
   * inside the range. On the opposite, this may be faster if you
   * need few indexes from wide array of values.
   * 
   * @param max maximum value (exclusive), output values will be from 0 to max-1
   * @param count how many integers we pick from the range
   * @return the randomized array
   */
  public static int[] uniqueInts(int max, int count) {
    Random rand = new Random();
    
    return uniqueInts(max, count, rand);
  }
  
  /**
   * Randomizes count integers which are unique from eachother. 
   * Integers are between 0 and max (exclusive). Implemented by 
   * checking the previous integers in the list so shuffling method
   * may be faster if you need to select high number of items 
   * inside the range. On the opposite, this may be faster if you
   * need few indexes from wide array of values.
   * 
   * @param max maximum value (exclusive), output values will be from 0 to max-1
   * @param count how many integers we pick from the range
   * @param rand as part of another randomized method same randomizer can be used
   * @return the randomized array
   */
  public static int[] uniqueInts(int max, int count, Random rand) {
    if (max < 0)
      throw new IllegalArgumentException("Max needs to be greater than zero.");
    
    if (count > max)
      throw new IllegalArgumentException("Randomizer cannot find more unique integers than there is values in the range.");
    
    int[] ret = new int[count];
    
    int i = 0;
    while (i < ret.length) {
      boolean ok = true;
      ret[i] = rand.nextInt(max);
      
      for (int j = 0; j < i; j++) {
        if (ret[i] == ret[j])
          ok = false;
      }
      
      if (ok)
        i++;
    }
    
    return ret;
  }
  
}
