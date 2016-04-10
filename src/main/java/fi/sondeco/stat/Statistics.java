package fi.sondeco.stat;

public class Statistics {

  public static double mean(double[] x) {
    double sum = 0;
    
    for (double d : x)
      sum += d;
    
    return sum / x.length;
  }

  /**
   * Sample variance
   * 
   * @param x
   * @return
   */
  public static double variance(double[] x) {
    double mean = mean(x);
    double sumDiff = 0;
    
    for (double d : x) {
      double diff = mean - d;
      sumDiff += diff * diff;
    }
    
    return sumDiff / (x.length - 1);
  }

  /**
   * Sample standard deviation.
   * 
   * Sample-based estimate of the standard deviation of the population.
   * 
   * @param x
   * @return
   */
  public static double standardDeviation(double[] x) {
    return Math.sqrt(variance(x));
  }

  public static double standardErrorOfMean(double stdDev, int n) {
    return stdDev / Math.sqrt(n);
  }
  
  public static double standardErrorOfMean(double[] x) {
    return standardErrorOfMean(standardDeviation(x), x.length);
  }
}
