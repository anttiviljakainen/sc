package fi.sondeco.neuralnetwork;

public class NeuralNetworkLayer {
  
  public NeuralNetworkLayer(NeuralNetworkActivationFunction activationFunction, int inputCount, int outputCount) {
    this.inputCount = inputCount;
    this.outputCount = outputCount;
    this.setActivationFunction(activationFunction);
  }

  public NeuralNetworkActivationFunction getActivationFunction() {
    return activationFunction;
  }

  public void setActivationFunction(NeuralNetworkActivationFunction activationFunction) {
    this.activationFunction = activationFunction;
  }

  public int getOutputCount() {
    return outputCount;
  }

  public int getInputCount() {
    return inputCount;
  }

  private NeuralNetworkActivationFunction activationFunction;
  private final int inputCount;
  private final int outputCount;
}
