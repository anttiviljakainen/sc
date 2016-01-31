package fi.sondeco.gradientdescent;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import fi.sondeco.linearregression.LinearRegressionCostFunction;
import fi.sondeco.matrix.Matrix;

public class TestGradientDescent {
  
  public static final String theta = "0\n0\n0";
  public static final String x = "3\t5\t6\n1\t2\t3\n9\t4\t2";
  public static final String y = "1\n6\n4";
  public static final double alpha = 0.01;
  public static final double lambda = 0;
  public static final int iterations = 1000;
  public static final String gradient = "0.49725\n17.04542";
  public static final String correctTheta = "1.2123\n-2.9458\n2.3219";

  
  @Test
  public void test() {
    BatchGradientDescent bgd = new BatchGradientDescent(new LinearRegressionCostFunction());
    
    try {
      Matrix newTheta = bgd.descent(new Matrix(x), new Matrix(y), new Matrix(theta), lambda, alpha, iterations);
      
      assertTrue(new Matrix(correctTheta).equals(newTheta, 0.001));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
