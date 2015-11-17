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
   * @param min minimum bounds for variables
   * @param max maximum bounds for variables
   * @param populationSize population size. Absolute minimum is 4, but for better convergence 10 is recommended minimum
   * @param maxIterations maximum amount of iterations to do
   * @param f mutation factor f = [0 2], generally 0.8 works ok
   * @param cr crossover constant = [0 1] 
   * @param e cost function to minimize
   * @return population after maxIterations
   */
  public static List<double[]> minimize(int parameterCount, double[] min, double[] max, int populationSize, 
      int maxIterations, double f, double cr, DifferentialEvolutionFunction function) {
    Random rand = new Random();
    
    List<double[]> population = generateInitialPopulation(populationSize, parameterCount, min, max, rand);
    
    for (int i = 0; i < maxIterations ; i++) {
      List<double[]> nextGen = new ArrayList<double[]>();
      
      for (double[] x : population) {
        double[] v = mutate(f, parameterCount, min, max, population, rand);
    
        double[] u = crossover(x, v, cr, rand);
        
        double ex = function.f(x);
        double eu = function.f(u);
        
        if (ex < eu)
          nextGen.add(x);
        else
          nextGen.add(u);
      }
      
      population = nextGen;
    }
    
    return population;
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
  
  private static double[] crossover(double[] x, double[] v, double cr, Random rand) {
    if (x.length > 1) {
      int vkeep = rand.nextInt(x.length);
      double[] trial = v.clone();
      
      for (int i = 0; i < trial.length; i++) {
        if ((rand.nextDouble() > cr) && (i != vkeep)) {
          trial[i] = x[i];
        }
      }
      
      return trial;
    } else
      return v;
  }
  
  private static double ensureInRange(double value, double min, double max) {
    if (value < min)
      return min;
    if (value > max)
      return max;
    return value;
  }
  
  private static List<double[]> generateInitialPopulation(int populationSize, int parameterCount, double[] min, 
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
