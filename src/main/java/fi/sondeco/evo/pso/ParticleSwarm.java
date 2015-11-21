package fi.sondeco.evo.pso;

import java.util.Random;

import fi.sondeco.matrix.Matrix;

/**
 * Particle Swarm Optimization
 * 
 * @author Antti Viljakainen
 */
public class ParticleSwarm {

  /**
   * Minimize cost function f according to given parameters.
   * 
   * Some guideline to the settings:
   * - particleCount is usually somewhere between 10 and 50
   * - particleBestModifier + globalBestModifier is usually 4. F.ex. 2 + 2 allows overshooting the best 
   * solutions thereby allowing exploration.
   * - Too high velocity can cause instability, too low velocity can make converge slow
   * 
   * @param dimension dimension of particle vectors
   * @param minBounds array size of dimension, describing the minimum boundaries of variable on the dimension
   * @param maxBounds array size of dimension, describing the maximum boundaries of variable on the dimension
   * @param particleCount count of particles
   * @param velocityModifier velocity modifier. Use 1 to ignore (use same particle velocity as basis).
   * @param particleBestModifier modifier on how important the particle considers it's own best solution
   * @param globalBestModifier modifier on how important the particle considers global best solution
   * @param maxIterations maximum iterations to make
   * @param f cost function to minimize
   * @return best global vector after maxIterations
   */
  public Matrix minimize(int dimension, double[] minBounds, double[] maxBounds, int particleCount, 
      double velocityModifier, double particleBestModifier, double globalBestModifier, 
      int maxIterations, PSOFunction f) {
    // Initialization
    Matrix particles = generateInitialParticles(dimension, particleCount, minBounds, maxBounds);
    Matrix particleBest = (Matrix) particles.clone();
    double[] particleBestCost = new double[particleCount];
    Matrix velocity = Matrix.rnd(particleCount, dimension, getRandom());
    
    Matrix globalBest = Matrix.expandRows(particles.row(0), particleCount);
    double globalBestCost = f.f(globalBest);
    particleBestCost[0] = globalBestCost;
    
    for (int i = 1; i < particles.getRows(); i++) {
      Matrix row = particles.row(i);
      particleBestCost[i] = f.f(row);
      
      if (particleBestCost[i] < globalBestCost) {
        globalBestCost = particleBestCost[i];
        globalBest = Matrix.expandRows(row, particleCount);
      }
    }
    
    int t = 0;
    while (t < maxIterations) {
      t++;
      
      Matrix r1 = Matrix.rnd(particleCount, dimension, getRandom()).multiply(particleBestModifier);
      Matrix r2 = Matrix.rnd(particleCount, dimension, getRandom()).multiply(globalBestModifier);
      
      velocity.multiply(velocityModifier);
      velocity.add(r1.dotmultiply(Matrix.subtract(particleBest, particles)));
      velocity.add(r2.dotmultiply(Matrix.subtract(globalBest, particles)));
      
      particles.add(velocity);

      ensureBounds(particles, minBounds, maxBounds);
      
      for (int i = 0; i < particles.getRows(); i++) {
        Matrix row = particles.row(i);
        double cost = f.f(row);
        
        if (cost < particleBestCost[i]) {
          particleBestCost[i] = cost;
          particleBest.setRow(i, row);
          
          if (cost < globalBestCost) {
            globalBestCost = cost;
            globalBest = Matrix.expandRows(row, particleCount);
          }
        }
      }
      
//      System.out.println("============= iteration " + t + " ============= globalBestCost = " + globalBestCost);
//      System.out.println(globalBest.row(0).toString());
    }
    
    return globalBest.row(0);
  }
  
  public Random getRandom() {
    return rand;
  }

  public void setRandomSeed(long seed) {
    rand = new Random(seed);
  }
  
  private Matrix generateInitialParticles(int dimension, int particleCount,
      double[] minBounds, double[] maxBounds) {
    Matrix particles = Matrix.rnd(particleCount, dimension, getRandom());
    
    for (int r = 0; r < particles.getRows(); r++) {
      for (int c = 0; c < particles.getColumns(); c++) {
        double range = maxBounds[c] - minBounds[c];
        
        particles.set(r, c, minBounds[c] + particles.get(r, c) * range);
      }
    }
    
    return particles;
  }

  private static void ensureBounds(Matrix particles, double[] min, double[] max) {
    for (int r = 0; r < particles.getRows(); r++) {
      for (int c = 0; c < particles.getColumns(); c++) {
        double value = particles.get(r, c);
        
        if (value < min[c])
          particles.set(r, c, min[c]);
        
        if (value > max[c])
          particles.set(r, c, max[c]);
      }
    }
  }

  private Random rand = new Random();
}
