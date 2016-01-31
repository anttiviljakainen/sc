package fi.sondeco.logisticregression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import fi.sondeco.matrix.Matrix;

public class TestLogisticRegression {

  public static final String theta = "-1\n0.2";
  public static final String x = "1\t34\n1\t35";
  public static final String y = "0\n1";
  public static final double lambda = 1.4;
  public static final String gradient = "0.49725\n17.04542";
  public static final String correcthypothesis = "0.99698\n0.99753";
  
  @Test
  public void testHypothesis() throws NumberFormatException, IOException {
    LogisticRegressionHypothesis hypothesis = new LogisticRegressionHypothesis();
    
    Matrix hx = hypothesis.hypothesis(new Matrix(theta), new Matrix(x));
    
    assertTrue(hx.equals(new Matrix(correcthypothesis), 0.00001));
  }
  
  @Test
  public void testCostFunction() throws NumberFormatException, IOException {
    LogisticRegressionCostFunction cost = new LogisticRegressionCostFunction();
    
    double j = cost.cost(new Matrix(theta), new Matrix(x), new Matrix(y), 0d);
    assertEquals(2.9027, j, 0.001d);
  }

  @Test
  public void testCostFunctionRegularized() throws NumberFormatException, IOException {
    LogisticRegressionCostFunction cost = new LogisticRegressionCostFunction();
    
    double j = cost.cost(new Matrix(theta), new Matrix(x), new Matrix(y), lambda);
    assertEquals(2.9167, j, 0.001d);
  }

  @Test
  public void testCostFunctionGradient() throws NumberFormatException, IOException {
    LogisticRegressionCostFunction cost = new LogisticRegressionCostFunction();
    
    Matrix gradients = cost.gradients(new Matrix(theta), new Matrix(x), new Matrix(y), lambda);
    
    Matrix correctGradient = new Matrix(gradient);
    
    assertTrue(correctGradient.equals(gradients, 0.00001));
  }

}
