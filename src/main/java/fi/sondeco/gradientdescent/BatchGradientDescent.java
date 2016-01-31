package fi.sondeco.gradientdescent;

import fi.sondeco.matrix.Matrix;

public class BatchGradientDescent {
  
  private CostFunction costFunction;
  private GradientDescentListener listener;

  public BatchGradientDescent(CostFunction costFunction) {
    this.costFunction = costFunction;
  }
  
  public BatchGradientDescent(CostFunction costFunction, GradientDescentListener listener) {
    this.costFunction = costFunction;
    this.listener = listener;
  }
  
  public Matrix descent(Matrix x, Matrix y, Matrix theta, double lambda, double alpha, int iterations) {
    Matrix new_theta = (Matrix) theta.clone();
    
    for (int i = 0; i < iterations; i++) {
      Matrix gradients = costFunction.gradients(new_theta, x, y, lambda);

      new_theta.subtract(gradients.multiply(alpha));
      
      if (listener != null) {
        listener.onIteration(x, y, new_theta, lambda, alpha, i + 1);
      }
    }
    
    return new_theta;
  }
  
}
