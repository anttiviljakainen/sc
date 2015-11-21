package fi.sondeco.matrix;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrix implements Cloneable {

  public Matrix(double scalar) {
    this.m = 1;
    this.n = 1;
    data = new double[m][n];
    set(0, 0, scalar);
  }
  
  public Matrix(int m, int n) {
    this.m = m;
    this.n = n;
    data = new double[m][n];
  }

  public Matrix(int m, int n, MatrixInit init) {
    this.m = m;
    this.n = n;
    data = new double[m][n];
    
    switch (init) {
      case ONES:
        for (int i = 0; i < m; i++) {
          for (int j = 0; j < n; j++) {
            set(i, j, 1);
          }
        }
      break;
      
      case RAND:
        Random r = new Random();
        for (int i = 0; i < m; i++) {
          for (int j = 0; j < n; j++) {
            set(i, j, r.nextDouble());
          }
        }
      break;
    }
  }
  
  public Matrix(int m, int n, Random rand) {
    this.m = m;
    this.n = n;
    data = new double[m][n];
    
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        set(i, j, rand.nextDouble());
      }
    }
  }
  
  public Matrix(String data) throws NumberFormatException, IOException {
    this(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
  }
  
  public Matrix(InputStream in) throws NumberFormatException, IOException {
    List<double[]> dataList = new ArrayList<double[]>();
    InputStreamReader reader = new InputStreamReader(in);
    try {
      BufferedReader input = new BufferedReader(reader);
      String line;
      while ((line = input.readLine()) != null) {
        String[] rowsTemp = line.split(";");
        for (String rowStr : rowsTemp) {
          String[] rowTemp = rowStr.trim().split("[\t| ]+");
          double[] row = new double[rowTemp.length];
          
          for (int i = 0; i < rowTemp.length; i++) {
            row[i] = Double.valueOf(rowTemp[i]);
          }
          
          dataList.add(row);
        }
      }
    } finally {
      reader.close();
    }
    
    m = dataList.size();
    n = dataList.get(0).length;
    
    this.data = new double[m][n];
    
    for (int i = 0; i < m; i++) {
      double[] row = dataList.get(i);
      
      for (int j = 0; j < n; j++) {
        data[i][j] = row[j];
      }
    }
  }

  public void addColumns(int index, int count) {
    this.n += count;
    
    for (int i = 0; i < m; i++) {
      double[] newDataRow = new double[this.n];

      if (index > 0)
        System.arraycopy(data[i], 0, newDataRow, 0, index);
      
      if (index < this.n - 1)
        System.arraycopy(data[i], index, newDataRow, index + count, n - count - index);
      
      this.data[i] = newDataRow;
    }
    
  }

  public void addRows(int index, int count) {
    this.m += count;
    
    double[][] newData = new double[this.m][this.n];
    
    for (int i = 0; i < index; i++) {
      newData[i] = this.data[i];
    }
    
    for (int i = this.m - 1; i > index; i--) {
      newData[i] = this.data[i - 1];
    }

    this.data = newData;
  }

  public Matrix appendRows(Matrix grad) {
    if (grad.getColumns() != getColumns())
      throw new RuntimeException("Column size must match to append rows.");
    
    this.m += grad.getRows();
    
    double[][] newData = new double[this.m][this.n];
    
    for (int i = 0; i < this.data.length; i++) {
      newData[i] = this.data[i];
    }

    for (int i = 0; i < grad.data.length; i++) {
      newData[this.data.length + i] = grad.data[i];
    }
    
    this.data = newData;
    
    return this;
  }
  
  @Override
  public Object clone() {
    try {
      Matrix x = (Matrix) super.clone();
      
      x.data = new double[m][n];
      
      for (int i = 0; i < m; i++)
        System.arraycopy(data[i], 0, x.data[i], 0, n);
      
      return x;
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static Matrix s(double scalar) {
    return new Matrix(scalar);
  }
  
  public static Matrix create(int m, int n, double scalar) {
    Matrix mx = new Matrix(m, n);
    
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        mx.set(i, j, scalar);
    
    return mx;
  }
  
  public static Matrix ones(int m, int n) {
    return new Matrix(m, n, MatrixInit.ONES);
  }

  public static Matrix rnd(int m, int n) {
    return new Matrix(m, n, MatrixInit.RAND);
  }
  
  public static Matrix rnd(int m, int n, Random rand) {
    return new Matrix(m, n, rand);
  }
  
  public static Matrix subtract(Matrix m1, Matrix m2) {
    Matrix ret = (Matrix) m1.clone();
    
    ret.subtract(m2);
    
    return ret;
  }
  
  public static Matrix multiply(Matrix m1, Matrix m2) {
    Matrix ret = (Matrix) m1.clone();
    
    ret.multiply(m2);
    
    return ret;
  }

  public static Matrix dotmultiply(Matrix m1, Matrix m2) {
    Matrix ret = (Matrix) m1.clone();
    
    ret.dotmultiply(m2);
    
    return ret;
  }
  
  
  public static Matrix multiply(Matrix m1, double scalar) {
    Matrix ret = (Matrix) m1.clone();
    
    ret.multiply(scalar);
    
    return ret;
  }
  
  public static Matrix log(Matrix m) {
    Matrix ret = (Matrix) m.clone();
    
    ret.log();
    
    return ret;
  }

  public static Matrix split(Matrix m, int firstRow, int firstColumn) {
    return split(m, firstRow, m.getRows() - 1, firstColumn, m.getColumns() - 1);
  }
  
  public static Matrix splitRow(Matrix m, int row) {
    return split(m, row, row, 0, m.getColumns() - 1);
  }

  public static Matrix splitColumn(Matrix m, int column) {
    return split(m, 0, m.getRows() - 1, column, column);
  }
  
  public static Matrix split(Matrix m, int firstRow, int lastRow, int firstColumn, int lastColumn) {
    Matrix split = new Matrix(lastRow - firstRow + 1, lastColumn - firstColumn + 1);
    
    // TODO: optimize
    for (int i = firstRow; i <= lastRow; i++) {
      for (int j = firstColumn; j <= lastColumn; j++) {
        split.set(i - firstRow, j - firstColumn, m.get(i, j));
      }
    }
    
    return split;
  }

  public static Matrix expandRows(Matrix row, int times) {
    Matrix m = new Matrix(row.getRows() * times, row.getColumns());
    
    for (int t = 0; t < times; t++) {
      for (int r = 0; r < row.getRows(); r++) {
        for (int c = 0; c < row.getColumns(); c++) {
          m.set(r + t * row.getRows(), c, row.get(r, c));
        }
      }
    }
    
    return m;
  }
  
  public static Matrix transpose(Matrix m) {
    Matrix ret = (Matrix) m.clone();
    ret.transpose();
    return ret;
  }
  
  
  public double get(int m, int n) {
    return data[m][n];
  }
  
  public Matrix row(int m) {
    return splitRow(this, m);
  }
  
  public Matrix column(int n) {
    return splitColumn(this, n);
  }
  
  public void set(int m, int n, double value) {
    data[m][n] = value;
  }

  public void setRow(int m, Matrix row) {
    for (int i = 0; i < n; i++) {
      set(m, i, row.get(0, i));
    }
  }
  
  public void setColumn(int n, Matrix column) {
    for (int i = 0; i < n; i++) {
      set(i, n, column.get(i, 0));
    }
  }
  
  public boolean isScalar() {
    return (m == 1) && (n == 1);
  }
  
  public double getScalar() {
    return get(0, 0);
  }
  
  public int getRows() {
    return m;
  }
  
  public int getColumns() {
    return n;
  }
  
  public Matrix transpose() {
    double[][] newData = new double[n][m];
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        newData[j][i] = data[i][j];
    }
    
    int temp = m;
    m = n;
    n = temp;
    data = newData;
    
    return this;
  }
  
  public Matrix add(double scalar) {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] += scalar;
    }
    
    return this;
  }
  
  public Matrix add(Matrix m) {
    return getImplementation().add(this, m);
  }
  
  public Matrix subtract(double scalar) {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] -= scalar;
    }
    
    return this;
  }
  
  public Matrix subtract(Matrix m) {
    return getImplementation().subtract(this, m);
  }

  public Matrix divide(double scalar) {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] /= scalar;
    }
    
    return this;
  }
  
  public Matrix multiply(double scalar) {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] *= scalar;
    }
    
    return this;
  }
  
  public Matrix multiply(Matrix m) {
    return getImplementation().multiply(this, m);
  }
  
  public Matrix dotmultiply(Matrix m) {
    return getImplementation().dotmultiply(this, m);
  }
  
  public Matrix dotdivide(Matrix m) {
    return getImplementation().dotdivide(this, m);
  }
  
  public Matrix dotpower(double power) {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] = Math.pow(data[i][j], power);
    }
    
    return this;
  }

  public Matrix exp() {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] = Math.exp(data[i][j]);
    }
    
    return this;
  }
  
  public Matrix log() {
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        data[i][j] = Math.log(data[i][j]);
    }
    
    return this;
  }

  public double sum() {
    double d = 0;
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        d += data[i][j];
    }
    
    return d;
  }

  public Matrix vectorize() {
    double[][] newdata = new double[this.m * this.n][1];
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        newdata[j * this.m + i][0] = data[i][j];
    }
    
    this.m = this.m * this.n;
    this.n = 1;
    this.data = newdata;
    
    return this;
  }
  
  public Matrix reshape(int rows, int columns) {
    double[][] newdata = new double[rows][columns];
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++) {
        int ind = i * this.n + j;
        
        int r = ind % rows;
        int c = ind / rows;
        
        newdata[r][c] = data[i][j];
      }
    }

    this.m = rows;
    this.n = columns;
    this.data = newdata;
    
    return this;
  }

  private MatrixImplementation getImplementation() {
    if (isScalar())
      return scalarImpl;
    else
      return defaultImpl;
  }
  
  @Override
  public boolean equals(Object obj) {
    Matrix m = (Matrix) obj;
    
    if ((getRows() != m.getRows()) || (getColumns() != m.getColumns()))
      return false;
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        if (m.get(i, j) != data[i][j])
          return false;
    }

    return true;   
  }
  
  public boolean equals(Matrix m, double delta) {
    if ((getRows() != m.getRows()) || (getColumns() != m.getColumns()))
      return false;
    
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++)
        if (Math.abs(m.get(i, j) - data[i][j]) > delta)
          return false;
    }

    return true;   
  }
  
  public void writeToStream(OutputStream out) throws IOException {
    OutputStreamWriter osw = new OutputStreamWriter(out);
    try {
      PrintWriter pw = new PrintWriter(osw);
      
      for (int i = 0; i < m; i++) {
        StringBuffer buffer = new StringBuffer();

        for (int j = 0; j < n - 1; j++) {
          buffer.append(data[i][j]);
          buffer.append(' ');
        }

        buffer.append(data[i][n - 1]);
        
        pw.println(buffer.toString());
      }
    } finally {
      osw.close();
    }
  }
  
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n - 1; j++) {
        buffer.append(data[i][j]);
        buffer.append(' ');
      }

      buffer.append(data[i][n - 1]);
      buffer.append("\n");
    }
    
    return buffer.toString();
  }

  private static final MatrixImplementation defaultImpl = new DefaultMatrixImplementation();
  private static final MatrixImplementation scalarImpl = new ScalarMatrixImplementation();
  
  int m;
  int n;
  double[][] data;
}
