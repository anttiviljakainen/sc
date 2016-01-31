package fi.sondeco.svm;

import fi.sondeco.machinelearning.Hypothesis;
import fi.sondeco.matrix.Matrix;

public class SupportVectorMachineHypothesis implements Hypothesis {

  public Matrix hypothesis(Matrix theta, Matrix x) {
    /**
     * Theta' * x
     * 
     * 1 if greater than 0
     * 0 otherwise
     */

    Matrix xT = Matrix.multiply(x, theta);

    return xT;
  }

}
