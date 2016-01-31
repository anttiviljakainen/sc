package fi.sondeco.linearregression;

import fi.sondeco.machinelearning.Hypothesis;
import fi.sondeco.matrix.Matrix;

public class LinearRegressionHypothesis implements Hypothesis {

  public Matrix hypothesis(Matrix theta, Matrix x) {
    // hX = theta' * x

    return Matrix.multiply(x, theta);
  }

}
