package fi.sondeco.evo;

import fi.sondeco.matrix.Matrix;

public class XSquaredObjectiveFunction implements ObjectiveFunction {

  public double f(Matrix x) {
    double d = 0;
    
    for (int i = 0; i < x.getColumns(); i++) {
      d += x.get(0, i) * x.get(0, i);
    }
    
    return d;
  }

}
