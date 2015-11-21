package fi.sondeco.evo.pso;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.sondeco.matrix.Matrix;

public class TestParticleSwarm {

  @Test
  public void test() {
    ParticleSwarm ps = new ParticleSwarm();

    ps.setRandomSeed(589021644);
    
    int dimensions = 2;
    double[] min = { -10, -10 };
    double[] max = { 10, 10 };
    int population = 10;
    double velocityModifier = 0.92d;
    double particleBestModifier = 2d;
    double globalBestModifier = 2d;
    int iterations = 100;
    
    PSOFunction f = new PSOFunction() {
      public double f(Matrix x) {
        double x1 = x.get(0, 0);
        double x2 = x.get(0, 1);
        
        return 4 * x1 * x1 + 3 * x2 * x2 - 6 * x1 * x2 - 4 * x1;
      }
    };
    
    Matrix result = ps.minimize(dimensions, min, max, population, velocityModifier, particleBestModifier, 
        globalBestModifier, iterations, f);
    
    assertEquals(-4, f.f(result), 0.001d);
    
    assertEquals(result.get(0, 0), 2, 0.1d);
    assertEquals(result.get(0, 1), 2, 0.1d);
  }

}
