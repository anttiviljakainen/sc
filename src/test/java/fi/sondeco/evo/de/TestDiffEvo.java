package fi.sondeco.evo.de;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fi.sondeco.evo.de.DifferentialEvolution;
import fi.sondeco.evo.de.DifferentialEvolutionFunction;

/**
 * 
 * Stuff to test: https://en.wikipedia.org/wiki/Test_functions_for_optimization
 */
public class TestDiffEvo {

  @Test
  public void testx2min() {
    int parameterCount = 1;
    double[] min = { -1 };
    double[] max = { 1 };
    int populationSize = 10;
    int maxIterations = 100;
    double f = 0.8;
    double cr = 0.5;
    DifferentialEvolutionFunction e = new DETestF();
    
    List<double[]> list = DifferentialEvolution.minimize(
        parameterCount, min, max, populationSize, maxIterations, f, cr, e);
    
    for (double[] x : list)
      // Convex functions should converge pretty close to zero
      assertEquals(0, x[0], 0.00001);
//      System.out.println(Arrays.toString(x));
  }

  @Test
  public void testp2() {
    int parameterCount = 2;
    double[] min = { -1, -1 };
    double[] max = { 1, 1 };
    int populationSize = 10;
    int maxIterations = 100;
    double f = 0.8;
    double cr = 0.5;
    DifferentialEvolutionFunction e = new DETestF();
    
    List<double[]> list = DifferentialEvolution.minimize(
        parameterCount, min, max, populationSize, maxIterations, f, cr, e);
    
    for (double[] x : list) {
      // Convex functions should converge pretty close to zero
      for (double d : x)
        assertEquals(0, d, 0.00001);
//    System.out.println(Arrays.toString(x));
    }
  }

  class DETestF implements DifferentialEvolutionFunction {
    public double f(double[] x) {
      double s = 0;
      for (int i = 0; i < x.length; i++) {
        s += x[i] * x[i];
      }
      return s;
    }
  }

  @Test
  public void test3() {
    int parameterCount = 2;
    double[] min = { -10, -10 };
    double[] max = { 10, 10 };
    int populationSize = 10;
    int maxIterations = 100;
    double f = 0.8;
    double cr = 0.5;
    
    List<double[]> list = DifferentialEvolution.minimize(
        parameterCount, min, max, populationSize, maxIterations, f, cr, 
        new DifferentialEvolutionFunction() {
          public double f(double[] x) {
            return 4 * x[0] * x[0] + 3 * x[1] * x[1] - 6 * x[0] * x[1] - 4 * x[0];
          }
        });

    for (double[] x : list) {
      System.out.println(Arrays.toString(x));

      // Convex functions should converge pretty close to zero
      for (double d : x)
        assertEquals(2, d, 0.001);
    }
  }
  
  @Test
  public void testSphere() {
    int parameterCount = 5;
    double[] min = { -5, -5, -5, -5, -5 };
    double[] max = { 5, 5, 5, 5, 5 };
    int populationSize = 10;
    int maxIterations = 200;
    double f = 0.8;
    double cr = 0.9;
    DifferentialEvolutionFunction e = new DETestF();
    
    List<double[]> list = DifferentialEvolution.minimize(
        parameterCount, min, max, populationSize, maxIterations, f, cr, e);

    System.out.println("Sphere output:");
    
    for (double[] x : list) {
      // Convex functions should converge pretty close to zero
      for (double d : x)
        assertEquals(0, d, 0.001);
      printArray(x, e);
    }
  }
  
  @Test
  public void testRosenbrock() {
    int parameterCount = 5;
    double[] min = { -5, -5, -5, -5, -5 };
    double[] max = { 5, 5, 5, 5, 5 };
    int populationSize = 10;
    int maxIterations = 2000;
    double f = 0.8;
    double cr = 0.9;
    
    DifferentialEvolutionFunction def = new DifferentialEvolutionFunction() {
      public double f(double[] x) {
        double sum = 0;
        for (int i = 0; i < x.length - 1; i++) {
          sum += 100 * Math.pow(x[i + 1] - Math.pow(x[i], 2), 2) + Math.pow(x[i] - 1, 2);
        }
        return sum;
      }
    };
    
    List<double[]> list = DifferentialEvolution.minimize(
        parameterCount, min, max, populationSize, maxIterations, f, cr, def);

    System.out.println("Rosenbrock output:");
    
    for (double[] x : list) {
//      for (double d : x)
//        assertEquals(1, d, 0.001);
      printArray(x, def);
    }
  }
  
  private void printArray(double[] arr, DifferentialEvolutionFunction def) {
    System.out.print("[");
    for (int i = 0; i < arr.length; i++) {
      System.out.print(String.format("%f", arr[i]));
      
      if (i != (arr.length - 1))
        System.out.print(" ");
    }
    System.out.print("]");
    
    if (def != null) {
      double fv = def.f(arr);
      
      System.out.print(" f(x)=");
      System.out.print(String.format("%f", fv));
    }
    
    System.out.println();
  }
}
