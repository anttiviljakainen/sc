package fi.sondeco.neuralnetwork;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.sondeco.gradientdescent.BatchGradientDescent;
import fi.sondeco.matrix.Matrix;

public class TestNeuralNetwork {

  private int input_layer_size = 2;
  private int hidden_layer_size = 2;
  private int num_labels = 4;
  private String nn_params = "0.1; 0.2; 0.3; 0.4; 0.5; 0.6; 0.7; 0.8; 0.9; 1.0; 1.1; 1.2; 1.3; 1.4; 1.5; 1.6; 1.7; 1.8";
  private String X = "1 2; 3 4; 5 6; 0 1; 1 2";  
  private String y = "4; 2; 3; 1; 2";
  private double lambda = 4;
  private double expectedCost = 17.441;
  private String expectedGradient = "0.42849 0.45184 0.59785 0.62285 1.18634 1.23469 0.74763 0.55926 0.76838 0.77550 1.54737 1.41502 1.65535 1.77867 1.89409 1.75478 2.01039 2.12420";

  public static final double GRADIENTDESCENT_alpha = 0.01;
  public static final double GRADIENTDESCENT_lambda = 0;
  public static final int GRADIENTDESCENT_iterations = 1000;
  
  private NeuralNetworkActivationFunction sigmoid = new SigmoidActivationFunction();
  private NeuralNetworkActivationFunction rectifier = new RectifierActivationFunction();
  private NeuralNetworkActivationFunction leakyReLU = new LeakyRectifierActivationFunction(0.01);
  
//  [J grad] = nnCostFunction(nn_params, input_layer_size, hidden_layer_size, num_labels, X, y, lambda)  
  
  @Test
  public void testCost() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(input_layer_size, getSigmoidLayers());
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    
    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    double cost2 = cost.cost(mTheta, mX, mY, lambda);
    
    assertEquals(expectedCost, cost2, 0.001);
  }

  @Test
  public void testGradient() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(input_layer_size, getSigmoidLayers());
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
    NeuralNetwork neuralNetwork = new NeuralNetwork(input_layer_size, getSigmoidLayers());
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
  
  @Test
  public void testSigmoidLearning() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(input_layer_size, getSigmoidLayers());
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    BatchGradientDescent bgd = new BatchGradientDescent(cost);

    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    try {
      double cost1 = cost.cost(mTheta, mX, mY, lambda);

      Matrix newTheta = bgd.descent(mX, mY, mTheta, 
          GRADIENTDESCENT_lambda, GRADIENTDESCENT_alpha, GRADIENTDESCENT_iterations);

      double cost2 = cost.cost(newTheta, mX, mY, lambda);
      
      assertTrue(cost2 < cost1);
//      assertTrue(new Matrix(correctTheta).equals(newTheta, 0.001));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private List<NeuralNetworkLayer> getRectifierLayers() {
    List<NeuralNetworkLayer> layers = new ArrayList<NeuralNetworkLayer>();
    layers.add(new NeuralNetworkLayer(rectifier, input_layer_size, hidden_layer_size));
    layers.add(new NeuralNetworkLayer(sigmoid, hidden_layer_size, num_labels));
    return layers;
  }
  
  private List<NeuralNetworkLayer> getLeakyReLULayers() {
    List<NeuralNetworkLayer> layers = new ArrayList<NeuralNetworkLayer>();
    layers.add(new NeuralNetworkLayer(leakyReLU, input_layer_size, hidden_layer_size));
    layers.add(new NeuralNetworkLayer(sigmoid, hidden_layer_size, num_labels));
    return layers;
  }
  
  private List<NeuralNetworkLayer> getSigmoidLayers() {
    List<NeuralNetworkLayer> layers = new ArrayList<NeuralNetworkLayer>();
    layers.add(new NeuralNetworkLayer(sigmoid, input_layer_size, hidden_layer_size));
    layers.add(new NeuralNetworkLayer(sigmoid, hidden_layer_size, num_labels));
    return layers;
  }
  
  @Test
  public void testRectifierLearning() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(input_layer_size, getRectifierLayers());
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    BatchGradientDescent bgd = new BatchGradientDescent(cost);

    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    try {
      double cost1 = cost.cost(mTheta, mX, mY, lambda);

      Matrix newTheta = bgd.descent(mX, mY, mTheta, 
          GRADIENTDESCENT_lambda, 
          GRADIENTDESCENT_alpha, 
          GRADIENTDESCENT_iterations);

      double cost2 = cost.cost(newTheta, mX, mY, lambda);
      
      assertTrue(cost2 < cost1);
//      assertTrue(new Matrix(correctTheta).equals(newTheta, 0.001));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Test
  public void testLeakyReLULearning() throws NumberFormatException, IOException {
    NeuralNetwork neuralNetwork = new NeuralNetwork(input_layer_size, getLeakyReLULayers());
    NeuralNetworkCostFunction cost = new NeuralNetworkCostFunction(neuralNetwork);
    BatchGradientDescent bgd = new BatchGradientDescent(cost);

    Matrix mTheta = new Matrix(nn_params);
    Matrix mX = new Matrix(X);
    Matrix mY = new Matrix(y);
    
    try {
      double cost1 = cost.cost(mTheta, mX, mY, lambda);

      Matrix newTheta = bgd.descent(mX, mY, mTheta, 
          GRADIENTDESCENT_lambda, 
          GRADIENTDESCENT_alpha, 
          GRADIENTDESCENT_iterations);

      double cost2 = cost.cost(newTheta, mX, mY, lambda);
      
      assertTrue(cost2 < cost1);
//      assertTrue(new Matrix(correctTheta).equals(newTheta, 0.001));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
