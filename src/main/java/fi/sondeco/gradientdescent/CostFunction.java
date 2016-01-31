package fi.sondeco.gradientdescent;

import fi.sondeco.matrix.Matrix;

public interface CostFunction {
  
  double cost(Matrix theta, Matrix x, Matrix y, double lambda);
  
  Matrix gradients(Matrix theta, Matrix x, Matrix y, double lambda);
}
