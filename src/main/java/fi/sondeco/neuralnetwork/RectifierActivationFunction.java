package fi.sondeco.neuralnetwork;

import fi.sondeco.matrix.Matrix;

public class RectifierActivationFunction implements NeuralNetworkActivationFunction {

  public Matrix activate(Matrix out) {
    Matrix c = (Matrix) out.clone();
    
    return c.dotmax(0);
  }

  public Matrix gradient(Matrix out) {
    Matrix m = (Matrix) out.clone();
//    x < 0  ---> 0
//    x >= 0 ---> 1
    
    for (int r = 0; r < m.getRows(); r++) {
      for (int c = 0; c < m.getColumns(); c++) {
        if (m.get(r, c) > 0)
          m.set(r, c, 1);
        else
          m.set(r, c, 0);
      }
    }

    return m;
  }
  
  
}
