package fi.sondeco.matrix;

import java.io.IOException;
import java.io.OutputStream;

public interface UnmodifiableMatrix {

  void addColumns(int index, int count);

  void addRows(int index, int count);

  Matrix appendRows(Matrix rows);

  Object clone();

  double get(int m, int n);

  Matrix row(int m);

  Matrix column(int n);

  boolean isScalar();

  double getScalar();

  int getRows();

  int getColumns();

  Matrix transpose();

  Matrix add(double scalar);

  Matrix add(Matrix m);

  Matrix subtract(double scalar);

  Matrix subtract(Matrix m);

  Matrix divide(double scalar);

  Matrix multiply(double scalar);

  Matrix multiply(Matrix m);

  Matrix dotmultiply(Matrix m);

  Matrix dotdivide(Matrix m);

  Matrix dotpower(double power);

  /**
   * Does dotwise max on the matrix i.e. picks the bigger one for the cell.
   * 
   * @param scalar
   * @return
   */
  Matrix dotmax(double scalar);

  Matrix each(MatrixCellOperator cellop);

  /**
   * Does dotwise min on the matrix i.e. picks the smaller one for the cell.
   * 
   * @param scalar
   * @return
   */
  Matrix dotmin(double scalar);

  Matrix exp();

  Matrix log();

  double sum();

  Matrix vectorize();

  Matrix reshape(int rows, int columns);

  boolean equals(Object obj);

  boolean equals(Matrix m, double delta);

  void writeToStream(OutputStream out) throws IOException;

  String toString();

}