package fi.sondeco.evo;

import java.util.Random;

public class Crossover {

  /**
   * Picks a random point in the parent vectors and does a crossover so that offspring 1
   * has beginning from parent 1 and tail of parent 2 and vice versa for offspring 2.
   * 
   * @param parent1
   * @param parent2
   * @param rand
   * @return
   */
  public static double[][] naiveCrossover(double[] parent1, double[] parent2, Random rand) {
    int len = parent1.length;
    
    double[] offspring1 = new double[len];
    double[] offspring2 = new double[len];
    
    int crossover = rand.nextInt(len);
    
    for (int i = 0; i < len; i++) {
      if (i < crossover) {
        offspring1[i] = parent1[i];
        offspring2[i] = parent2[i];
      } else {
        offspring1[i] = parent2[i];
        offspring2[i] = parent1[i];
      }
    }
    
    return new double[][] {
        offspring1,
        offspring2
    };
  }
  
}
