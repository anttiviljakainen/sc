package fi.sondeco.linearregression;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import fi.sondeco.linearregression.LinearRegressionCostFunction;
import fi.sondeco.logisticregression.LogisticRegressionHypothesis;
import fi.sondeco.matrix.Matrix;

public class TestLinearRegression {

  public static final String theta = "-1\n0.2";
  public static final String x = "1\t34\n1\t35";
  public static final String y = "0\n1";
  public static final double lambda = 1.4;
  public static final String gradient = "0.49725\n17.04542";
  public static final String correcthypothesis = "5.8\n6.0";
  
  @Test
  public void testHypothesis() throws NumberFormatException, IOException {
    LinearRegressionHypothesis hypothesis = new LinearRegressionHypothesis();
    
    Matrix hx = hypothesis.hypothesis(new Matrix(theta), new Matrix(x));
    
    assertTrue(hx.equals(new Matrix(correcthypothesis), 0.001));
  }
  
  @Test
  public void testCostFunction() {
    LinearRegressionCostFunction cost = new LinearRegressionCostFunction();
    
    try {
      double j = cost.cost(new Matrix(theta), new Matrix(x), new Matrix(y), 0d);
      assertEquals(2.9027, j, 0.001d);
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testCostFunctionRegularized() {
    LinearRegressionCostFunction cost = new LinearRegressionCostFunction();
    
    try {
      double j = cost.cost(new Matrix(theta), new Matrix(x), new Matrix(y), lambda);
      assertEquals(2.9167, j, 0.001d);
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testCostFunctionGradient() {
    LinearRegressionCostFunction cost = new LinearRegressionCostFunction();
    
    try {
      Matrix gradients = cost.gradients(new Matrix(theta), new Matrix(x), new Matrix(y), lambda);
      
      Matrix correctGradient = new Matrix(gradient);
      
      assertTrue(correctGradient.equals(gradients, 0.00001));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
