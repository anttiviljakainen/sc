package fi.sondeco.matrix;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class TestScalarMatrix {

  public static final String matrix1b = "1 2; 1 2; 1 2"; // 3x2 matrix
  
  private double getRnd(double mult) {
    Random R = new Random();
    return R.nextDouble() * mult;
  }
  
  @Test
  public void testScalarAdd() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1b);
    double d = getRnd(10);
    
    Matrix a1 = Matrix.create(3, 2, d);
    Matrix a2 = Matrix.s(d);
    
    Matrix z1 = a1.add(m);
    Matrix z2 = a2.add(m);
    assertTrue(z1.equals(z2, 0.001));
  }
  
  @Test
  public void testScalarSubtract() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1b);
    double d = getRnd(10);
    
    Matrix a1 = Matrix.create(3, 2, d);
    Matrix a2 = Matrix.s(d);
    
    Matrix z1 = a1.subtract(m);
    Matrix z2 = a2.subtract(m);
    assertTrue(z1.equals(z2, 0.001));
  }  

  @Test
  public void testScalarDotmultiply() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1b);
    double d = getRnd(10);
    
    Matrix a1 = Matrix.create(3, 2, d);
    Matrix a2 = Matrix.s(d);
    
    Matrix z1 = a1.dotmultiply(m);
    Matrix z2 = a2.dotmultiply(m);
    assertTrue(z1.equals(z2, 0.001));
  }  

  @Test
  public void testScalarDotDivide() throws NumberFormatException, IOException {
    Matrix m = new Matrix(matrix1b);
    double d = getRnd(10);
    
    Matrix a1 = Matrix.create(3, 2, d);
    Matrix a2 = Matrix.s(d);
    
    Matrix z1 = a1.dotdivide(m);
    Matrix z2 = a2.dotdivide(m);
    assertTrue(z1.equals(z2, 0.001));
  }  

}
