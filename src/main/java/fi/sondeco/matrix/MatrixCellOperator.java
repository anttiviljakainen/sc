package fi.sondeco.matrix;

public interface MatrixCellOperator {
  /**
   * Performs operation to a cell value.
   * 
   * @param val value in the cell
   * @return new value for the cell
   */
  double op(double val);
}
