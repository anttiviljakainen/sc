package fi.sondeco.evo.pso;

import fi.sondeco.matrix.Matrix;

public interface PSOFunction {
  
  /**
   * Returns cost of candidate x
   * 
   * @param x row parameter vector
   * @return cost/fitness of vector x
   */
  double f(Matrix x);
}
