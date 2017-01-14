package fi.sondeco.opt.testfun;

import fi.sondeco.opt.GradientObjectiveFunction;

/**
 * Objective function with minimum at [0, 0, ... 0]. Works dynamically with
 * any-dimensional inputs.
 */
public class SphereObjectiveFunction implements GradientObjectiveFunction {

  public double f(double[] x) {
    double sum = 0;
    
    for (int i = 0; i < x.length; i++)
      sum += x[i] * x[i];
    
    return sum;
  }

  public double[] gradient(double[] x) {
    double[] grad = new double[x.length];
    
    for (int i = 0; i < x.length; i++)
      grad[i] = 2 * x[i];
    
    return grad;
  }

}
