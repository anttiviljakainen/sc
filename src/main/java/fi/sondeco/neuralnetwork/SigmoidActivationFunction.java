package fi.sondeco.neuralnetwork;

import fi.sondeco.matrix.Matrix;

/**
 * Sigmoid activation function for neural networks.
 * 
 * @author Antti Viljakainen
 */
public class SigmoidActivationFunction implements
    NeuralNetworkActivationFunction {

  public Matrix activate(Matrix out) {
    Matrix m = Matrix.multiply(out, -1).exp().add(1);
    
    return Matrix.s(1).dotdivide(m);
  }

  public Matrix derivate(Matrix out) {
//    gz = 1 ./ (1 + exp(-z));
//    g = gz .* (1 - gz);

    Matrix gz = Matrix.s(1).dotdivide(Matrix.s(1).add(Matrix.multiply(out, -1).exp()));
    
    return gz.dotmultiply(Matrix.s(1).subtract(gz));
  }

}
