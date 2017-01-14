package fi.sondeco.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.sondeco.matrix.Matrix;

public class NeuralNetwork {

  public NeuralNetwork(int inputCount, List<NeuralNetworkLayer> layers) {
    this.inputCount = inputCount;
    this.layers.addAll(layers);
  }

//  public int getOutputCount() {
//    return getLastLayer().getNumberofNeurons();
//  }
  
//  public int[] getNeuronCounts() {
//    return neuronCounts;
//  }

  public int getInputCount() {
    return inputCount;
  }

  public List<NeuralNetworkLayer> getLayers() {
    return Collections.unmodifiableList(this.layers);
  }
  
  public NeuralNetworkLayer getLayer(int layer) {
    return layers.get(layer);
  }
  
  public int getLayerCount() {
    return layers.size();
  }
  
  public NeuralNetworkActivationFunction getActivationFunction(int layer) {
    return layers.get(layer).getActivationFunction();
  }

  public int getOutputCount() {
    return getLastLayer().getOutputCount();
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
    
    for (int i = 0; i < layers.size(); i++) {
      Matrix theta = thetas.get(i);
      
      a.addRows(0, 1);
      a.set(0, 0, 1);

      a = Matrix.multiply(theta, a);
      
      a = getActivationFunction(i).activate(a);
    }

    return a;
  }
  
  private NeuralNetworkLayer getLastLayer() {
    return getLayer(layers.size() - 1);
  }
  
  private List<Matrix> reshapeThetas(Matrix vectorizedTheta) {
    List<Matrix> thetas = new ArrayList<Matrix>();
    
    int index = 0;
    
    for (int i = 0; i < getLayerCount(); i++) {
      int n1 = getLayer(i).getInputCount() + 1;
      int n2 = getLayer(i).getOutputCount();
      
      Matrix vector = Matrix.split(vectorizedTheta, index, index + n2 * n1 - 1, 0, 0);
      vector.reshape(n2, n1);
      thetas.add(vector);
      index += n2 * n1;
    }
    
    return thetas;
  }
  
  private List<NeuralNetworkLayer> layers = new ArrayList<NeuralNetworkLayer>();
  private final int inputCount;
}
