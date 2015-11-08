package fi.sondeco.diffevo;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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
    public double energy(double[] x) {
      double s = 0;
      for (int i = 0; i < x.length; i++) {
        s += x[i] * x[i];
      }
      return s;
    }
  }
  
}
