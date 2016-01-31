package fi.sondeco.neuralnetwork;

import fi.sondeco.matrix.Matrix;

public class RectifierActivationFunction implements NeuralNetworkActivationFunction {

  public Matrix activate(Matrix out) {
    Matrix c = (Matrix) out.clone();
    
    return c.dotmax(0);
  }

  public Matrix derivate(Matrix out) {
    Matrix c = (Matrix) out.clone();
    asdasd
    
    x < 0  ---> 0
    x >= 0 ---> 1
    
    return c;
  }
  
  
}
