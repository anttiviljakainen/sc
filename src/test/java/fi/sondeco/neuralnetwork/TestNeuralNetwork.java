package fi.sondeco.neuralnetwork;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import fi.sondeco.matrix.Matrix;

public class TestNeuralNetwork {

  private int input_layer_size = 2;
  private int hidden_layer_size = 2;
  private int num_labels = 4;
  private String nn_params = "0.1; 0.2; 0.3; 0.4; 0.5; 0.6; 0.7; 0.8; 0.9; 1.0; 1.1; 1.2; 1.3; 1.4; 1.5; 1.6; 1.7; 1.8";
  private String X = "1  2 ; 3  4 ; 5  6; 0 1 ; 1 2";  
  private String y = "4; 2; 3; 1; 2";
  private double lambda = 4;
  private double expectedCost = 17.441;
  private String expectedGradient = "0.42849 0.45184 0.59785 0.62285 1.18634 1.23469 0.74763 0.55926 0.76838 0.77550 1.54737 1.41502 1.65535 1.77867 1.89409 1.75478 2.01039 2.12420";

  private SigmoidActivationFunction sigmoid = new SigmoidActivationFunction();
  
//  [J grad] = nnCostFunction(nn_params, input_layer_size, hidden_layer_size, num_labels, X, y, lambda)  
  
  @Test
  public void testCost() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(sigmoid, input_layer_size, hidden_layer_size, num_labels);
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    
    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    double cost2 = cost.cost(mTheta, mX, mY, lambda);
    
    assertEquals(expectedCost, cost2, 0.001);
  }

  @Test
  public void testGradient() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(sigmoid, input_layer_size, hidden_layer_size, num_labels);
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    
    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    Matrix gradients = cost.gradients(mTheta, mX, mY, lambda);
    
    Matrix expGrad = new Matrix(expectedGradient);
    expGrad.transpose();
    
    assertTrue(gradients.equals(expGrad, 0.0001));
  }

  @Test
  public void testGradientChecking() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(sigmoid, input_layer_size, hidden_layer_size, num_labels);
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    
    double epsilon = 0.0001;
    
    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    Matrix gradients = cost.gradients(mTheta, mX, mY, lambda);
    Matrix gradApprox = new Matrix(mTheta.getRows(), mTheta.getColumns());
    
    for (int i = 0; i < mTheta.getRows(); i++) {
      Matrix mPlus = (Matrix) mTheta.clone();
      Matrix mMinus = (Matrix) mTheta.clone();
      
      mPlus.set(i, 0, mTheta.get(i, 0) + epsilon);
      mMinus.set(i, 0, mTheta.get(i, 0) - epsilon);
      
      gradApprox.set(i, 0, (cost.cost(mPlus, mX, mY, lambda) - cost.cost(mMinus, mX, mY, lambda)) / (2 * epsilon));
    }

    assertTrue(gradients.equals(gradApprox, 0.00001));
  }
  
}
