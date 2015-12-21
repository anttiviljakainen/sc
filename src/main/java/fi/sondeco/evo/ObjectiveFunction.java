package fi.sondeco.evo;

import fi.sondeco.matrix.Matrix;

public interface ObjectiveFunction {
  
  /**
   * Returns cost of candidate x
   * 
   * @param x row parameter vector
   * @return cost of vector x
   */
  double f(Matrix x);
}
