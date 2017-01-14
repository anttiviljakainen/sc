package fi.sondeco.opt;

public class OptimizationJob {
  
  public OptimizationJob(ObjectiveFunction objectiveFunction, double[] initialGuess) {
    this.setObjectiveFunction(objectiveFunction);
    this.setInitialGuess(initialGuess);
  }
  
  public ObjectiveFunction getObjectiveFunction() {
    return objectiveFunction;
  }
  public void setObjectiveFunction(ObjectiveFunction objectiveFunction) {
    this.objectiveFunction = objectiveFunction;
  }

  public double[] getInitialGuess() {
    return initialGuess;
  }

  public void setInitialGuess(double[] initialGuess) {
    this.initialGuess = initialGuess;
  }

  private ObjectiveFunction objectiveFunction;
  private double[] initialGuess;
}
