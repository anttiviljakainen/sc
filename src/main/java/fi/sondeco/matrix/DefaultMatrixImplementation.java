package fi.sondeco.matrix;

class DefaultMatrixImplementation implements MatrixImplementation {

  public DefaultMatrixImplementation() {
  }
  
  public Matrix add(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.add(m.getScalar());

    for (int i = 0; i < matrix.m; i++) {
      for (int j = 0; j < matrix.n; j++)
        matrix.data[i][j] += m.get(i, j);
    }
    
    return matrix;
  }

  public Matrix subtract(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.subtract(m.getScalar());
    
    for (int i = 0; i < matrix.m; i++) {
      for (int j = 0; j < matrix.n; j++)
        matrix.data[i][j] -= m.get(i, j);
    }
    
    return matrix;
  }

  public Matrix multiply(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.multiply(m.getScalar());
    
    int newM = matrix.m;
    int newN = m.getColumns();
    double[][] newData = new double[newM][newN];
    
    for (int i = 0; i < matrix.m; i++) {
      for (int j = 0; j < matrix.n; j++) {
        for (int k = 0; k < m.getColumns(); k++) {
          newData[i][k] += matrix.data[i][j] * m.get(j, k);
        }
      }
    }
    
    matrix.m = newM;
    matrix.n = newN;
    matrix.data = newData;
    
    return matrix;
  }

  public Matrix dotmultiply(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.multiply(m.getScalar());
    
    for (int i = 0; i < matrix.m; i++) {
      for (int j = 0; j < matrix.n; j++)
        matrix.data[i][j] *= m.get(i, j);
    }
    
    return matrix;
  }

  public Matrix dotdivide(Matrix matrix, Matrix m) {
    if (m.isScalar())
      return matrix.divide(m.getScalar());

    for (int i = 0; i < matrix.m; i++) {
      for (int j = 0; j < matrix.n; j++)
        matrix.data[i][j] /= m.get(i, j);
    }
    
    return matrix;
  }

}
