package fi.sondeco.opt;

public interface Optimizer {

  double[] minimize(OptimizationJob job);
  
}
