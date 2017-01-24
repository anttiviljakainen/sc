package fi.sondeco.machinelearning;

import fi.sondeco.matrix.Matrix;

public class HoughVote {
  
  public HoughVote(int rho, double angle, int votes) {
    this.rho = rho;
    this.angle = angle;
    this.votes = votes;
  }
  
  public int getRho() {
    return rho;
  }

  public double getAngle() {
    return angle;
  }

  public int getVotes() {
    return votes;
  }

  public Matrix getDirectionVector() {
    Matrix m = new Matrix(1, 2);
    m.set(0, 0, Math.cos(Math.toRadians(angle)));
    m.set(0, 1, Math.sin(Math.toRadians(angle)));
    return m;
  }
  
  public Matrix getPositionVector() {
    Matrix m = getDirectionVector();
    return m.multiply(rho);
  }

  public Matrix getLineVector() {
    Matrix m = new Matrix(1, 2);
    m.set(0, 0, Math.sin(Math.toRadians(angle)));
    m.set(0, 1, -Math.cos(Math.toRadians(angle)));
    return m;
  }
  
  private final int rho;
  private final double angle;
  private final int votes;
}
