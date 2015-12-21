package fi.sondeco.evo;

import fi.sondeco.matrix.Matrix;

public interface ConstraintFunction {
  
  /**
   * Returns true if the given parameter vector is feasible solution
   * 
   * @param x
   * @return
   */
  boolean isFeasible(Matrix x);

  /**
   * Returns the violation of the constraint
   * 
   * @param x
   * @return
   */
  double violation(Matrix x);
}
