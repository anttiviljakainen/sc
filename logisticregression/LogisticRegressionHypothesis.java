package fi.sondeco.logisticregression;

import fi.sondeco.machinelearning.Hypothesis;
import fi.sondeco.matrix.Matrix;
import fi.sondeco.matrix.MatrixInit;

public class LogisticRegressionHypothesis implements Hypothesis {

  public Matrix hypothesis(Matrix theta, Matrix x) {
    // hX = 1 ./ (1 + exp(-(X * theta)));

    Matrix xT = Matrix.multiply(x, theta);
    xT.multiply(-1);
    xT.exp();
    xT.add(1);
    
    Matrix hX = new Matrix(xT.getRows(), xT.getColumns(), MatrixInit.ONES);
    
    hX.dotdivide(xT);
    
    return hX;
  }

}
