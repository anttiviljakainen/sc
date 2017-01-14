package fi.sondeco.matrix;

public class LUDecomposition {
  
  public LUDecomposition(Matrix L, Matrix U, Matrix P) {
    this.P = P;
    this.U = U;
    this.L = L;
  }
  
  public Matrix getL() {
    return L;
  }

  public Matrix getU() {
    return U;
  }

  public Matrix getP() {
    return P;
  }

  private final Matrix L;
  private final Matrix U;
  private final Matrix P;
}
