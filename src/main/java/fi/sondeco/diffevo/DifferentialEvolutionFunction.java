package fi.sondeco.diffevo;

public interface DifferentialEvolutionFunction {
  
  /**
   * Returns energy of vector x
   * 
   * @param x parameter vector
   * @return energy of vector x
   */
  double energy(double[] x);
}
