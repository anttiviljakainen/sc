package fi.sondeco.evo;

import java.util.Random;

public class Mutation {

  public static double[] polynomialMutation(double[] parent, double[] lowerBound, double[] upperBound, 
      double nm, double mutationProbability, Random rand) {
    double[] ret = new double[parent.length];
    
    for (int i = 0; i < parent.length; i++) {
      if (rand.nextDouble() < mutationProbability)
        ret[i] = polynomialMutation(parent[i], lowerBound[i], upperBound[i], nm, rand);
      else
        ret[i] = parent[i];
    }
    
    return ret;
  }
  
  
  /**
   * Returns mutated value for variable near given parent value.
   * 
   * Typical values for nm are [20, 100]. 20 might be place to start.
   * 
   * @param parent
   * @param lowerBound
   * @param upperBound
   * @param nm modifier for how diverse solutions are created
   * @param rand
   * @return
   */
  public static double polynomialMutation(double parent, double lowerBound, double upperBound, double nm, Random rand) {
    double u = rand.nextDouble();
    
    if (u <= 0.5) {
      double delta = Math.pow(2 * u, 1 / (1 + nm)) - 1;
      
      return parent + delta * (parent - lowerBound);
    } else {
      double delta = 1 - Math.pow(2 * (1 - u), 1 / (1 + nm));
      
      return parent + delta * (upperBound - parent);
    }
  }
  
}
