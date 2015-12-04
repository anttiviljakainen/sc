package fi.sondeco.evo.simann;

import java.util.Arrays;
import java.util.Random;

public class SimAnn {

  public SimAnn() {
    // TODO Auto-generated constructor stub
  }

  public double energy(double[] x) {
//    double sum = 0;
//    
//    for (double d : x)
//      sum += d;
//    
//    return sum;  
    return 4 * Math.pow(x[0], 2) + 3 * Math.pow(x[1], 2) - 6 * x[0] * x[1] - 4 * x[0];
  }
  
  public double temperature(int k, int kmax, double previousTemp) {
    return previousTemp * 0.97;
  }
  
  private int nextDifferentIndex(Random rand, int len, int prev) {
    int ret;
    
    do {
      ret = rand.nextInt(len);
    } while (ret == prev);
    
    return ret;
  }
  
  private double bounds(double x, double min, double max) {
    return Math.min(Math.max(x, min), max);
  }
  
  public double[] neighbour(double[] s) {
    double[] neighbour = s.clone();
    
    Random rand = new Random();
    
//    if (rand.nextDouble() < 0.9) {
//      if (neighbour.length == 2) {
//        neighbour[0] = s[1];
//        neighbour[1] = s[0];
//      } else if (neighbour.length > 2) {
//        int i1 = rand.nextInt(neighbour.length);
//        int i2 = nextDifferentIndex(rand, neighbour.length, i1);
//        
//        neighbour[i1] = s[i2];
//        neighbour[i2] = s[i1];
//      }
//    } else {
      int r = rand.nextInt(neighbour.length);
      
      neighbour[r] += rand.nextDouble() * 16 - neighbour[r];
      
//      if (rand.nextBoolean()) {
//        double range = 16 - neighbour[r];
//        
//        neighbour[r] += range * rand.nextDouble();
//      } else {
//        neighbour[r] -= neighbour[r] * rand.nextDouble();
//      }
      
//      neighbour[r] = bounds(rand.nextBoolean() ? neighbour[r] + rand.nextDouble() : neighbour[r] - rand.nextDouble(), 0, 16);
//    }
    
    return neighbour;
  }
  
  private double p(double energy, double energy2, double t) {
//    if (energy2 < energy)
//      return 1;
//    else
      return Math.exp(-(energy2 - energy) / t);
  }
  
  public double[] minimize() {
    Random r = new Random();
    double[] s = new double[] { 8, 8 };

    int maxK = 1000;
    double t = 20; // TODO: initial temperature
    
    for (int k = 0; k < maxK; k++) {
      double[] s_new = neighbour(s);
      
      double rnd = r.nextDouble();
      
      double e1 = energy(s);
      double e2 = energy(s_new);
      
      if (e2 < e1) {
        s = s_new;
      } else if (p(energy(s), energy(s_new), t) >= rnd)
        s = s_new;

      // Temperature for the next iteration
      t = temperature(k, maxK, t);
      
      System.out.println((k + 1) + " " + Arrays.toString(s) + " " + energy(s));
    }
    
    return s;
  }
}
