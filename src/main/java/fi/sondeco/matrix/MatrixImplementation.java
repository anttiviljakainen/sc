package fi.sondeco.matrix;

public interface MatrixImplementation {

  Matrix add(Matrix matrix, Matrix m);
  Matrix subtract(Matrix matrix, Matrix m);
  Matrix multiply(Matrix matrix, Matrix m);

  Matrix dotmultiply(Matrix matrix, Matrix m);
  Matrix dotdivide(Matrix matrix, Matrix m);
  
}
