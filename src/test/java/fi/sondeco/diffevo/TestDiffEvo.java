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
    DifferentialEvolutionFunction e = new DETestF();
    
    List<double[]> list = DifferentialEvolution.minimize(parameterCount, min, max, populationSize, maxIterations, f, e);
    
    for (double[] x : list)
      assertEquals(0, x[0], 0.00001);
//      System.out.println(Arrays.toString(x));
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
