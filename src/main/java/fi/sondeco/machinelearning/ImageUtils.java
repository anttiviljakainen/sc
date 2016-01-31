package fi.sondeco.machinelearning;

import java.awt.image.BufferedImage;

public class ImageUtils {

  public static int[] histogramGS(BufferedImage image) {
    int[] ret = new int[256];
    
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int rgb = image.getRGB(x, y);
        
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        
        ret[(r + g + b) / 3]++;
      }
    }

    return ret;
  }
  
  public static double[] normalizedHistogram(int[] histogram, int pixelCount) {
    double[] ret = new double[histogram.length];
    
    for (int i = 0; i < histogram.length; i++) {
      ret[i] = (double) histogram[i] / pixelCount;
    }
    
    return ret;
  }
  
  public static double[] cumulativeHistogram(double[] normalizedHistogram) {
    double[] ret = new double[normalizedHistogram.length];
    
    ret[0] = normalizedHistogram[0];
    for (int i = 1; i < normalizedHistogram.length; i++) {
      ret[i] = ret[i - 1] + normalizedHistogram[i];
    }
    
    return ret;
  }
  
  /**
   * Applies global histogram equalization to image. Turns the image to grayscale.
   * 
   * @param image
   * @return
   */
  public static BufferedImage equalizeHistogramGS(BufferedImage image) {
    BufferedImage bf = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    
    int[] histogramGS = histogramGS(image);
    double[] normalizedHistogram = normalizedHistogram(histogramGS, image.getWidth() * image.getHeight());
    double[] cumulativeHistogram = cumulativeHistogram(normalizedHistogram);
    
    for (int i = 0; i < bf.getWidth(); i++) {
      for (int j = 0; j < bf.getHeight(); j++) {

        int rgb = image.getRGB(i, j);
        
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
    
        int pixel = (r + g + b) / 3;
        int pixelVal = (int) (cumulativeHistogram[pixel] * 255);
        
        bf.getRaster().setPixel(i, j, new int[] { pixelVal });
      }
    }

    return bf;
  }
  
}
