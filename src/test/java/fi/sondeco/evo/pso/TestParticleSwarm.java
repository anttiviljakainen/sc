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

  @Test
  public void testSphere() {
    ParticleSwarm ps = new ParticleSwarm();

//    ps.setRandomSeed(589021644);
    
    int dimensions = 5;
    double[] min = { -5, -5, -5, -5, -5 };
    double[] max = { 5, 5, 5, 5, 5 };
    int population = 10;
    double velocityModifier = 0.6d;
    double particleBestModifier = 2d;
    double globalBestModifier = 2d;
    int iterations = 100;
    
    PSOFunction f = new PSOFunction() {
      public double f(Matrix x) {
        double s = 0;
        for (int i = 0; i < x.getColumns(); i++) {
          s += x.get(0, i) * x.get(0, i);
        }
        return s;
      }
    };
    
    Matrix result = ps.minimize(dimensions, min, max, population, velocityModifier, particleBestModifier, 
        globalBestModifier, iterations, f);
    
    System.out.println("Sphere output:");
    System.out.println(result);
    System.out.println(String.format("%f", f.f(result)));
    
    assertEquals(0, f.f(result), 0.001d);
    
    assertEquals(0, result.get(0, 0), 0.1d);
    assertEquals(0, result.get(0, 1), 0.1d);
  }

  @Test
  public void testRosenbrock() {
    ParticleSwarm ps = new ParticleSwarm();

//    ps.setRandomSeed(589021644);
    
    int dimensions = 5;
    double[] min = { -5, -5, -5, -5, -5 };
    double[] max = { 5, 5, 5, 5, 5 };
    int population = 50;
    double velocityModifier = 0.5d;
    double particleBestModifier = 2d;
    double globalBestModifier = 2d;
    int iterations = 10000;
    
    PSOFunction f = new PSOFunction() {
      public double f(Matrix x) {
        double sum = 0;
        for (int i = 0; i < x.getColumns() - 1; i++) {
          sum += 100 * Math.pow(x.get(0, i + 1) - Math.pow(x.get(0, i), 2), 2) + Math.pow(x.get(0, i) - 1, 2);
        }
        return sum;
      }
    };
    
    Matrix result = ps.minimize(dimensions, min, max, population, velocityModifier, particleBestModifier, 
        globalBestModifier, iterations, f);
    
    System.out.println("Rosenbrock output:");
    System.out.println(result);
    System.out.println(String.format("%f", f.f(result)));

    assertEquals(0, f.f(result), 0.01d);
    
    assertEquals(1, result.get(0, 0), 0.1d);
    assertEquals(1, result.get(0, 1), 0.1d);
    assertEquals(1, result.get(0, 2), 0.1d);
    assertEquals(1, result.get(0, 3), 0.1d);
    assertEquals(1, result.get(0, 4), 0.1d);
  }
  
}
