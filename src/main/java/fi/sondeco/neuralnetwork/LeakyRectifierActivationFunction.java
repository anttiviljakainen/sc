package fi.sondeco.neuralnetwork;

import fi.sondeco.matrix.Matrix;
import fi.sondeco.matrix.MatrixCellOperator;

public class LeakyRectifierActivationFunction implements NeuralNetworkActivationFunction {

  private double leakMultiplier;

  /**
   * Creates new Leaky Rectifier Activation Function.
   * 
   * @param leakMultiplier leak when value at neuron is less than 0, 0.01 is typical starting point
   */
  public LeakyRectifierActivationFunction(double leakMultiplier) {
    this.leakMultiplier = leakMultiplier;
  }
  
  public Matrix activate(Matrix out) {
    Matrix c = (Matrix) out.clone();
    
    return c.each(new MatrixCellOperator() {
      public double op(double val) {
        if (val < 0)
          return val * leakMultiplier;
        else
          return val;
      }
    });
  }

  public Matrix gradient(Matrix out) {
    Matrix m = (Matrix) out.clone();
    // x <= 0  ---> leakMultiplier
    // x > 0 ---> 1
    
    for (int r = 0; r < m.getRows(); r++) {
      for (int c = 0; c < m.getColumns(); c++) {
        if (m.get(r, c) > 0)
          m.set(r, c, 1);
        else
          m.set(r, c, leakMultiplier);
      }
    }

    return m;
  }
  
  
}
