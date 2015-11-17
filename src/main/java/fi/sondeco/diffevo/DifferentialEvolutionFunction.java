package fi.sondeco.diffevo;

public interface DifferentialEvolutionFunction {
  
  /**
   * Returns cost / fitness of candidate x
   * 
   * @param x parameter vector
   * @return cost/fitness of vector x
   */
  double f(double[] x);
}
