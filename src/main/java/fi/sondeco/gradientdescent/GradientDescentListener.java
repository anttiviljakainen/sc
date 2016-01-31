package fi.sondeco.gradientdescent;

import fi.sondeco.matrix.Matrix;

public interface GradientDescentListener {

  void onIteration(Matrix x, Matrix y, Matrix currentTheta, double lambda, double alpha, int iteration);
}
