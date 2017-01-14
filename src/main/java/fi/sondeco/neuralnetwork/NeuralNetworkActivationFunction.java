package fi.sondeco.neuralnetwork;

import fi.sondeco.matrix.Matrix;

public interface NeuralNetworkActivationFunction {
  
  Matrix activate(Matrix out);
  
  Matrix gradient(Matrix out);
  
}
