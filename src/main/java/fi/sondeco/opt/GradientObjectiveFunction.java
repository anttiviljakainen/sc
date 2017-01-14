package fi.sondeco.opt;

public interface GradientObjectiveFunction extends ObjectiveFunction {
  
  double[] gradient(double[] x);
  
}
