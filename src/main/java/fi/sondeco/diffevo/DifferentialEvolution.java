package fi.sondeco.diffevo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.sondeco.sc.Randomizer;

public class DifferentialEvolution {

  /**
   * Minimize the function e using Differential Evolution algorithm.
   * 
   * @param parameterCount amount of parameters in parameter vector(s)
   * @param populationSize population size. Absolute minimum is 4, but for better convergence 10 is recommended minimum
   * @param maxIterations maximum amount of iterations to do
   * @param f mutation factor f = [0 2], generally 0.8 works ok 
   * @param e cost/energy function to minimize
   * @return population after maxIterations
   */
  public static List<double[]> minimize(int parameterCount, double[] min, double[] max, int populationSize, int maxIterations, double f, DifferentialEvolutionFunction e) {
    Random rand = new Random();
    
    List<double[]> gen = generateInitialData(populationSize, parameterCount, min, max, rand);
    
    for (int i = 0; i < maxIterations ; i++) {
      List<double[]> nextGen = new ArrayList<double[]>();
      
      for (double[] x : gen) {
        double[] u = mutate(f, parameterCount, min, max, gen, rand);
        
        double ex = e.energy(x);
        double eu = e.energy(u);
        
        if (ex < eu)
          nextGen.add(x);
        else
          nextGen.add(u);
      }
      
      gen = nextGen;
    }
    
    return gen;
  }

  /**
   * Creates mutated vector based on four random vectors in the population.
   * 
   * @param f
   * @param parameterCount
   * @param min
   * @param max
   * @param gen
   * @param rand
   * @return
   */
  private static double[] mutate(double f, int parameterCount, double[] min, double[] max, 
      List<double[]> gen, Random rand) {
    int[] rows = Randomizer.uniqueInts(gen.size(), 4, rand);
    
    double[] m = new double[parameterCount];
    
    double[] x1 = gen.get(rows[0]);
    double[] x2 = gen.get(rows[1]);
    double[] x3 = gen.get(rows[2]);
    
    for (int i = 0; i < parameterCount; i++) {
      m[i] = ensureInRange(x1[i] + f * (x2[i] - x3[i]), min[i], max[i]);
    }
    
    return m;
  }
  
  private static double ensureInRange(double value, double min, double max) {
    if (value < min)
      return min;
    if (value > max)
      return max;
    return value;
  }
  
  private static List<double[]> generateInitialData(int populationSize, int parameterCount, double[] min, 
      double[] max, Random rand) {
    List<double[]> data = new ArrayList<double[]>();
    
    for (int i = 0; i < populationSize; i++) {
      double[] x = new double[parameterCount];
      for (int j = 0; j < parameterCount; j++) {
        double range = max[j] - min[j];
        
        x[j] = rand.nextDouble() * range + min[j];
      }
      data.add(x);
    }
    return data;
  }
  
}
