package fi.sondeco.machinelearning;

import fi.sondeco.matrix.Matrix;

public interface Hypothesis {
  
  Matrix hypothesis(Matrix theta, Matrix x);
  
}
