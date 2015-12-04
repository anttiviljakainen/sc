package fi.sondeco.evo.simann;

public class OptFunction {

  public OptFunction() {
  }
  
  public double f(double[] x) {
    double sum = 0;
    
    for (double d : x)
      sum += d;
    
    return sum;  
  }

}
