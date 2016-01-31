package fi.sondeco.svm;

import fi.sondeco.gradientdescent.CostFunction;
import fi.sondeco.logisticregression.LogisticRegressionHypothesis;
import fi.sondeco.matrix.Matrix;
import fi.sondeco.matrix.MatrixInit;

public class SupportVectorMachineCostFunction implements CostFunction {

  private LogisticRegressionHypothesis hypothesis;

  public SupportVectorMachineCostFunction() {
    this.hypothesis = new LogisticRegressionHypothesis();
  }
  
  public double cost(Matrix theta, Matrix x, Matrix y, double lambda) {
    // hX = 1 ./ (1 + exp(-(X * theta)));

    Matrix hX = hypothesis.hypothesis(theta, x);

    // J = 1 / m * sum(-y .* log(hX) - (1 - y) .* log(1 - hX)) 
    
    Matrix a1 = Matrix.multiply(y, -1);
    a1.dotmultiply(Matrix.log(hX));
    
    Matrix a2 = new Matrix(y.getRows(), y.getColumns(), MatrixInit.ONES);
    a2.subtract(y);
    
    Matrix a3 = new Matrix(hX.getRows(), hX.getColumns(), MatrixInit.ONES);
    a3.subtract(hX);
    a3.log();
    
    a2.dotmultiply(a3);
    
    a1.subtract(a2);
    
    // lambda in svm = "C"
    a1.multiply(lambda);
    
    double j = a1.sum();

    // Theta' * Theta
    
    Matrix thetaSplit = Matrix.split(theta, 1, theta.getRows() - 1, 0, theta.getColumns() - 1);
    thetaSplit.dotpower(2d);
    
    double reg = 0.5 * thetaSplit.sum(); 
        
    return j + reg;
  }

  public Matrix gradients(Matrix theta, Matrix x, Matrix y, double lambda) {
    //grad = (1 / m * ((hX - y)' * X)') + lambda / m * [0; theta(2:length(theta),:)];
    
    Matrix hX = hypothesis.hypothesis(theta, x);
    int m = x.getRows();
    
    hX.subtract(y);
    hX.transpose();
    hX.multiply(x);
    hX.transpose();
    hX.multiply(1d / m);

    Matrix thetaClone = (Matrix) theta.clone();
    for (int i = 0; i < thetaClone.getColumns(); i++)
      thetaClone.set(0, i, 0);
    thetaClone.multiply(lambda / m);
    
    hX.add(thetaClone);
    
    return hX;
  }

}
