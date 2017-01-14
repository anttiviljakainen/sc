package fi.sondeco.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import fi.sondeco.gradientdescent.CostFunction;
import fi.sondeco.matrix.Matrix;

public class NeuralNetworkCostFunction implements CostFunction {

//  private LogisticRegressionHypothesis hypothesis;

  private NeuralNetwork neuralNetwork;

  public NeuralNetworkCostFunction(NeuralNetwork neuralNetwork) {
    this.neuralNetwork = neuralNetwork;
  }
  
  public double cost(Matrix thetaVector, Matrix x, Matrix y, double lambda) {
    // m = number of examples
    
    int m = x.getRows();
    
    List<Matrix> thetas = reshapeThetas(thetaVector);
    
    double J = 0;
    
    for (int i = 0; i < m; i++) {
      Matrix a = Matrix.split(x, i, i, 0, x.getColumns() - 1);
      a.transpose();

      for (int layer = 0; layer < neuralNetwork.getLayerCount(); layer++) {
        Matrix theta = thetas.get(layer);
        a.addRows(0, 1);
        a.set(0, 0, 1);

        a = Matrix.multiply(theta, a);
        a = neuralNetwork.getLayer(layer).getActivationFunction().activate(a);
      }

      Matrix yX = new Matrix(neuralNetwork.getOutputCount(), 1);
      if (neuralNetwork.getOutputCount() != 1) {
        double d = y.get(i, 0);
        yX.set((int) d - 1, 0, 1);
      } else {
        yX.set(0, 0, y.get(i, 0));
      }
      
      Matrix b1 = Matrix.dotmultiply(yX, Matrix.log(a));
//      Matrix b2 = Matrix.ones(yX.getRows(), yX.getColumns()).subtract(yX).dotmultiply(Matrix.ones(a.getRows(), a.getColumns()).subtract(a).log());
      Matrix sub = Matrix.s(1).subtract(a);
      Matrix b2 = Matrix.s(1).subtract(yX).dotmultiply(sub.log());
      
      J = J - 1d / m * b1.add(b2).sum();
    }

    double reg = 0;
    
    for (Matrix t : thetas) {
      Matrix t2 = Matrix.split(t, 0, 1);
      reg += t2.dotpower(2).sum();
    }
    
    J = J + lambda / (2d * m) * reg;
    
    return J;
  }

  private List<Matrix> reshapeThetas(Matrix vectorizedTheta) {
//    List<Matrix> thetas = new ArrayList<Matrix>();
//    
//    int index = 0;
//    
//    // TODO: remove getThetas dependency
//    for (Matrix theta : neuralNetwork.getThetas()) {
//      Matrix vector = Matrix.split(vectorizedTheta, index, index + theta.getRows() * theta.getColumns() - 1, 0, 0);
//      vector.reshape(theta.getRows(), theta.getColumns());
//      thetas.add(vector);
//      index += theta.getRows() * theta.getColumns();
//    }
//    
//    return thetas;
    List<Matrix> thetas = new ArrayList<Matrix>();
    
    int index = 0;
    
    for (int i = 0; i < neuralNetwork.getLayerCount(); i++) {
      int n1 = neuralNetwork.getLayer(i).getInputCount() + 1;
      int n2 = neuralNetwork.getLayer(i).getOutputCount();
      
      Matrix vector = Matrix.split(vectorizedTheta, index, index + n2 * n1 - 1, 0, 0);
      vector.reshape(n2, n1);
      thetas.add(vector);
      index += n2 * n1;
    }
    
    return thetas;
  }
  
  public Matrix gradients(Matrix thetaVector, Matrix x, Matrix y, double lambda) {
    
//    Theta1_grad = 1 / m * Delta2 + lambda * Theta1;
//    Theta2_grad = 1 / m * Delta3 + lambda * Theta2;
    
    // m = number of examples
    
    int m = x.getRows();
    
    List<Matrix> thetas = reshapeThetas(thetaVector);
    List<Matrix> deltas = new ArrayList<Matrix>();
    
    for (int i = 0; i <= thetas.size() - 1; i++) {
      Matrix tl = thetas.get(i);
      deltas.add(new Matrix(tl.getRows(), tl.getColumns()));
    }
    
//    double J = 0;
    
    
    for (int i = 0; i < m; i++) {
      Matrix activation = Matrix.split(x, i, i, 0, x.getColumns() - 1);
      
      activation.transpose();
      activation.addRows(0, 1);
      activation.set(0, 0, 1);

      // Parameters per level before activation function
      List<Matrix> Z = new ArrayList<Matrix>();
      // Activations per level
      List<Matrix> A = new ArrayList<Matrix>();
      
      A.add(activation);
      
      for (int layer = 0; layer < neuralNetwork.getLayerCount(); layer++) {
        Matrix theta = thetas.get(layer);
        Matrix z;
        
        Z.add(z = Matrix.multiply(theta, activation));
        
        activation = neuralNetwork.getLayer(layer).getActivationFunction().activate(z);
        
        if (layer < thetas.size() - 1) {
          activation.addRows(0, 1);
          activation.set(0, 0, 1);
        }
        
        A.add(activation);
      }

      Matrix yX = new Matrix(neuralNetwork.getOutputCount(), 1);
      
      if (neuralNetwork.getOutputCount() != 1) {
        double d = y.get(i, 0);
        yX.set((int) d - 1, 0, 1);
      } else {
        yX.set(0, 0, y.get(i, 0));
      }

//      Matrix b1 = Matrix.dotmultiply(yX, Matrix.log(a));
//      Matrix b2 = Matrix.ones(yX.getRows(), yX.getColumns()).subtract(yX).dotmultiply(Matrix.ones(a.getRows(), a.getColumns()).subtract(a).log());
//      
//      J = J - 1 / m * b1.add(b2).sum();
          
//      J = J - 1 / m * sum(
//          yX .* log(hX) + 
//          (1 - yX) .* log(1 - hX));

      
//      Matrix dm = A.get(A.size() - 1).subtract(yX);
      Matrix dm = yX;
      Matrix al = A.get(A.size() - 1);
      Matrix ap = (Matrix) A.get(A.size() - 2).clone();
      Matrix err = al.subtract(dm);
      Matrix delta = deltas.get(deltas.size() - 1);
      
//      ap.addRows(0, 1);
//      ap.set(0, 0, 1);
      ap.transpose();
      
      delta.add(Matrix.multiply(err, ap));

      for (int j = thetas.size() - 2; j >= 0; j--) {
        Matrix tl = thetas.get(j + 1);
        delta = deltas.get(j);

        Matrix z = (Matrix) Z.get(j).clone();

        z = neuralNetwork.getLayer(j + 1).getActivationFunction().gradient(z);
        
        z.addRows(0, 1);
        z.set(0, 0, 1);
//        al = A.get(j);
        
        err = Matrix.transpose(tl).multiply(err).dotmultiply(z);
        err = Matrix.split(err, 1, 0);
        
        ap = (Matrix) A.get(j).clone();
//        ap.addRows(0, 1);
//        ap.set(0, 0, 1);
        ap.transpose();
//        asd
        delta.add(Matrix.multiply(err, ap));
      }
      
//      d3 = a3 - yX;
//      Delta3 = Delta3 + d3 * [1; a2]';

//      d2 = Theta2' * d3 .* [1; sigmoidGradient(z2)];
//      d2 = d2(2:end);
//      Delta2 = Delta2 + d2 * [1; a1]';
      
    }

    Matrix vectorized_gradient = new Matrix(0, 1);
    
    for (int i = 0; i < deltas.size(); i++) {
      Matrix delta = deltas.get(i);
      Matrix tl = thetas.get(i);
      
      for (int j = 0; j < tl.getRows(); j++)
        tl.set(j, 0, 0d);
      
      Matrix grad = delta.add(Matrix.multiply(tl, lambda)).multiply(1d / m);
      
      grad.vectorize();
      
      vectorized_gradient.appendRows(grad);
    }

    return vectorized_gradient;
  }

}
