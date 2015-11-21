package fi.sondeco.matrix;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TestMatrix {

  public static final String vector1 = "1\t2\t3"; // 1x3 vector
  public static final String matrix1 = "1\t2\n1\t2\n1\t2"; // 3x2 matrix
  public static final String matrix1b = "1 2; 1 2; 1 2"; // 3x2 matrix
  
  @Test
  public void testCreate() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1);
    
    assertEquals(3, m.getRows());
    assertEquals(2, m.getColumns());
  }

  @Test
  public void testCreateParser() throws NumberFormatException, IOException {
    Matrix m1 = new Matrix(matrix1);
    Matrix m2 = new Matrix(matrix1b);
    
    assertEquals(3, m1.getRows());
    assertEquals(2, m1.getColumns());

    assertEquals(3, m2.getRows());
    assertEquals(2, m2.getColumns());
  }
  
  @Test
  public void testClone() throws NumberFormatException, IOException, CloneNotSupportedException {
    Matrix m = new Matrix(matrix1);
    Matrix m2 = (Matrix) m.clone();
    
    assertEquals(m, m2);
  }
  
  @Test
  public void testAddColumns() throws NumberFormatException, IOException, CloneNotSupportedException {
    Matrix m = new Matrix(matrix1);
    
    m.addColumns(0, 1);
    
    assertEquals(3, m.getColumns());
    assertEquals(0, m.get(0, 0), 0d);
    assertEquals(0, m.get(1, 0), 0d);
    assertEquals(0, m.get(2, 0), 0d);

    m = new Matrix(matrix1);
    m.addColumns(1, 1);
    
    assertEquals(3, m.getColumns());
    assertEquals(0, m.get(0, 1), 0d);
    assertEquals(0, m.get(1, 1), 0d);
    assertEquals(0, m.get(2, 1), 0d);

    m = new Matrix(matrix1);
    m.addColumns(2, 1);
    
    assertEquals(3, m.getColumns());
    assertEquals(0, m.get(0, 2), 0d);
    assertEquals(0, m.get(1, 2), 0d);
    assertEquals(0, m.get(2, 2), 0d);
  }
  
  @Test
  public void testAddRows() throws NumberFormatException, IOException, CloneNotSupportedException {
    Matrix m = new Matrix(matrix1);
    
    m.addRows(0, 1);
    
    assertEquals(4, m.getRows());
    assertEquals(0, m.get(0, 0), 0d);
    assertEquals(0, m.get(0, 1), 0d);

    m = new Matrix(matrix1);
    m.addRows(1, 1);
    
    assertEquals(4, m.getRows());
    assertEquals(0, m.get(1, 0), 0d);
    assertEquals(0, m.get(1, 1), 0d);

    m = new Matrix(matrix1);
    m.addRows(3, 1);
    
    assertEquals(4, m.getRows());
    assertEquals(0, m.get(3, 0), 0d);
    assertEquals(0, m.get(3, 1), 0d);
  }
  
  @Test
  public void testVectorize() throws NumberFormatException, IOException, CloneNotSupportedException {
    Matrix m = new Matrix(matrix1);
    
    m.vectorize();
    
    assertEquals(1, m.getColumns());
    assertEquals(6, m.getRows());
    assertEquals(1, m.get(0, 0), 0d);
    assertEquals(1, m.get(1, 0), 0d);
    assertEquals(1, m.get(2, 0), 0d);
    assertEquals(2, m.get(3, 0), 0d);
    assertEquals(2, m.get(4, 0), 0d);
    assertEquals(2, m.get(5, 0), 0d);
  }

  @Test
  public void testReshape() throws NumberFormatException, IOException, CloneNotSupportedException {
    Matrix m = new Matrix(matrix1);
    
    m.vectorize();
    
    m.reshape(3, 2);
    
    assertEquals(3, m.getRows());
    assertEquals(2, m.getColumns());

    assertEquals(1, m.get(0, 0), 0d);
    assertEquals(1, m.get(1, 0), 0d);
    assertEquals(1, m.get(2, 0), 0d);
    assertEquals(2, m.get(0, 1), 0d);
    assertEquals(2, m.get(1, 1), 0d);
    assertEquals(2, m.get(2, 1), 0d);
  }
  
  @Test
  public void testSplit() throws NumberFormatException, IOException, CloneNotSupportedException {
    Matrix m = new Matrix(matrix1);
    Matrix m2 = Matrix.split(m, 1, m.getRows() - 1, 1, m.getColumns() - 1);
    
    assertEquals(2, m2.getRows());
    assertEquals(1, m2.getColumns());
    assertEquals(2, m2.get(0, 0), 0d);
    assertEquals(2, m2.get(1, 0), 0d);
  }
  
  @Test
  public void testTranspose() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1);
    
    m.transpose();
    
    assertEquals(2, m.getRows());
    assertEquals(3, m.getColumns());
    assertEquals(1, m.get(0, 0), 0);
    assertEquals(1, m.get(0, 1), 0);
    assertEquals(1, m.get(0, 2), 0);
    assertEquals(2, m.get(1, 0), 0);
    assertEquals(2, m.get(1, 1), 0);
    assertEquals(2, m.get(1, 2), 0);
  }

  @Test
  public void testAddScalar() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1);
    
    m.add(0.5);
    
    assertEquals(1.5, m.get(0, 0), 0);
    assertEquals(2.5, m.get(0, 1), 0);
    assertEquals(1.5, m.get(1, 0), 0);
    assertEquals(2.5, m.get(1, 1), 0);
    assertEquals(1.5, m.get(2, 0), 0);
    assertEquals(2.5, m.get(2, 1), 0);
  }

  @Test
  public void testSubtractScalar() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1);
    
    m.subtract(0.5);
    
    assertEquals(0.5, m.get(0, 0), 0);
    assertEquals(1.5, m.get(0, 1), 0);
    assertEquals(0.5, m.get(1, 0), 0);
    assertEquals(1.5, m.get(1, 1), 0);
    assertEquals(0.5, m.get(2, 0), 0);
    assertEquals(1.5, m.get(2, 1), 0);
  }

  @Test
  public void testMultiplyScalar() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1);
    
    m.multiply(0.5);
    
    assertEquals(0.5, m.get(0, 0), 0);
    assertEquals(1, m.get(0, 1), 0);
    assertEquals(0.5, m.get(1, 0), 0);
    assertEquals(1, m.get(1, 1), 0);
    assertEquals(0.5, m.get(2, 0), 0);
    assertEquals(1, m.get(2, 1), 0);
  }
  
  @Test
  public void testMultiplyMatrix() throws NumberFormatException, IOException {
    Matrix m = new Matrix(vector1);
    Matrix m2 = new Matrix(vector1);
    
    m.transpose();
    
    m.multiply(m2);
    
    assertEquals(1, m.get(0, 0), 0);
    assertEquals(2, m.get(0, 1), 0);
    assertEquals(3, m.get(0, 2), 0);
    
    assertEquals(2, m.get(1, 0), 0);
    assertEquals(4, m.get(1, 1), 0);
    assertEquals(6, m.get(1, 2), 0);

    assertEquals(3, m.get(2, 0), 0);
    assertEquals(6, m.get(2, 1), 0);
    assertEquals(9, m.get(2, 2), 0);
  }

  @Test
  public void testMultiplyMatrix2() throws NumberFormatException, IOException {
    Matrix m = new Matrix(vector1);
    Matrix m2 = new Matrix(vector1);
    
    m2.transpose();
    
    m.multiply(m2);
    
    assertTrue(m.isScalar());
    assertEquals(14, m.getScalar(), 0);
  }

  @Test
  public void testDotMultiplyMatrix() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1);
    
    m.dotmultiply(m);
    
    assertEquals(1, m.get(0, 0), 0);
    assertEquals(4, m.get(0, 1), 0);

    assertEquals(1, m.get(1, 0), 0);
    assertEquals(4, m.get(1, 1), 0);

    assertEquals(1, m.get(2, 0), 0);
    assertEquals(4, m.get(2, 1), 0);
  }

  @Test
  public void testExpandRows() throws NumberFormatException, IOException {
    Matrix rowVector = new Matrix(vector1);
    
    Matrix m = Matrix.expandRows(rowVector, 5);

    assertEquals(m.getRows(), 5);
    assertEquals(m.getColumns(), rowVector.getColumns());

    for (int i = 0; i < m.getRows(); i++) {
      for (int j = 0; j < m.getColumns(); j++) {
        assertEquals(rowVector.get(0, j), m.get(i, j), 0.000000001);
      }
    }
  }
  
}
