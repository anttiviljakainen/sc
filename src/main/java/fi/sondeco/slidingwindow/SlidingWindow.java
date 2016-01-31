package fi.sondeco.slidingwindow;

import java.awt.image.BufferedImage;
import java.util.Iterator;

import fi.sondeco.matrix.Matrix;

public class SlidingWindow implements Iterator<Matrix> {

  private BufferedImage image;
  private int w;
  private int h;
  private int step;
  private int x = 0;
  private int y = 0;
  
  public SlidingWindow(BufferedImage image, int w, int h, int step) {
    this.image = image;
    this.w = w;
    this.h = h;
    this.step = step;
  }
  
  public Matrix next() {
    Matrix m = new Matrix(h, w);
    
    // TODO: optimize
    
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        int rgb = image.getRGB(this.x + x, this.y + y);
        
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        
        m.set(y, x, (r + g + b) / 3);
      }
    }

    this.x += this.step;
    
    if (this.x + this.w > image.getWidth()) {
      this.x = 0;
      this.y += this.step;
    }
    
    return m;
  }

  public boolean hasNext() {
    return this.y + this.h <= image.getHeight();
  }

  public Rectangle getCurrentRect() {
    return new Rectangle(x, y, w, h);
  }
}
