package fi.sondeco.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import fi.sondeco.matrix.Matrix;

public class NeuralNetwork {

  public NeuralNetwork(NeuralNetworkActivationFunction activationFunction, int ... neuronCounts) {
    this.activationFunction = activationFunction;
    this.neuronCounts = neuronCounts;
  }

  public int getOutputCount() {
    return neuronCounts[neuronCounts.length - 1];
  }
  
  public int[] getNeuronCounts() {
    return neuronCounts;
  }

  public NeuralNetworkActivationFunction getActivationFunction() {
    return activationFunction;
  }

  public void setActivationFunction(NeuralNetworkActivationFunction activationFunction) {
    this.activationFunction = activationFunction;
  }

  /**
   * 
   * @param x row vector with the parameters
   * @return column vector with the output activation
   */
  public Matrix hypothesis(Matrix vectorizedTheta, Matrix x) {
    Matrix a = (Matrix) x.clone();
    a.transpose();
    
    List<Matrix> thetas = reshapeThetas(vectorizedTheta);
    
    for (Matrix theta : thetas) {
      a.addRows(0, 1);
      a.set(0, 0, 1);

      a = Matrix.multiply(theta, a);
      
      a = getActivationFunction().activate(a);
    }

    return a;
  }
  
  private List<Matrix> reshapeThetas(Matrix vectorizedTheta) {
    List<Matrix> thetas = new ArrayList<Matrix>();
    
    int index = 0;
    
    for (int i = 0; i < neuronCounts.length - 1; i++) {
      int n1 = neuronCounts[i] + 1;
      int n2 = neuronCounts[i + 1];
      
      Matrix vector = Matrix.split(vectorizedTheta, index, index + n2 * n1 - 1, 0, 0);
      vector.reshape(n2, n1);
      thetas.add(vector);
      index += n2 * n1;
    }
    
    return thetas;
  }
  
  private NeuralNetworkActivationFunction activationFunction;
  private int[] neuronCounts = new int[0];
}
