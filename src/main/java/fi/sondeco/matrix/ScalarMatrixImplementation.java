package fi.sondeco.matrix;

class ScalarMatrixImplementation implements MatrixImplementation {

  public Matrix add(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.add(m.getScalar());

    return setDimensions(matrix, m, matrix.getScalar()).add(m);
  }

  public Matrix subtract(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.subtract(m.getScalar());

    return setDimensions(matrix, m, matrix.getScalar()).subtract(m);
  }

  public Matrix multiply(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.multiply(m.getScalar());
    
    return setDimensions(matrix, m, matrix.getScalar()).dotmultiply(m);
  }

  public Matrix dotmultiply(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.multiply(m.getScalar());

    return setDimensions(matrix, m, matrix.getScalar()).dotmultiply(m);
  }

  public Matrix dotdivide(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.divide(m.getScalar());
    
    return setDimensions(matrix, m, matrix.getScalar()).dotdivide(m);
  }

  private Matrix setDimensions(Matrix matrix, Matrix m, double scalar) {
    matrix.m = m.m;
    matrix.n = m.n;
    matrix.data = new double[matrix.m][matrix.n];

    for (int i = 0; i < matrix.m; i++)
      for (int j = 0; j < matrix.n; j++)
        matrix.data[i][j] = scalar;
   
    return matrix;
  }
  
}
