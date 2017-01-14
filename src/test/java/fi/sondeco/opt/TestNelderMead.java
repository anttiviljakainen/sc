package fi.sondeco.opt;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import fi.sondeco.opt.testfun.SphereObjectiveFunction;

public class TestNelderMead {

  @Test
  public void testSphere() {
    NelderMead nm = new NelderMead();
    
    double[] x0 = { 1, 2, 3 };
    
    System.out.println("3D Sphere test");
    OptimizationJob job = new OptimizationJob(new SphereObjectiveFunction(), x0);
    double[] result = nm.minimize(job);
    System.out.println("3D Sphere test result " + Arrays.toString(result));

    for (int i = 0; i < result.length; i++)
      assertEquals(0, result[i], 0.001);
  }
  
}
