package fi.sondeco.machinelearning;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

  /**
   * Simple transform of pixel rgb value into grayscale value.
   * 
   * @param rgb
   * @return
   */
  public static int grayscale(int rgb) {
    int r = (rgb >> 16) & 255;
    int g = (rgb >> 8) & 255;
    int b = rgb & 255;

    return (r + g + b) / 3;
  }
  
  /**
   * Grayscale gradient-based edge detection.
   */
  public static BufferedImage gradientEdgeDetectGS(BufferedImage image) {
    BufferedImage bf = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    
    for (int x = 1; x < bf.getWidth() - 1; x++) {
      for (int y = 1; y < bf.getHeight() - 1; y++) {
        double xGradient = -0.5d * grayscale(image.getRGB(x - 1, y)) + 0.5d * grayscale(image.getRGB(x + 1, y));
        double yGradient = -0.5d * grayscale(image.getRGB(x, y - 1)) + 0.5d * grayscale(image.getRGB(x, y + 1));
        
        double gradientMagnitude = Math.sqrt(xGradient * xGradient + yGradient * yGradient);
        int pixelVal = (int) gradientMagnitude;
        
        bf.getRaster().setPixel(x, y, new int[] { pixelVal });
      }
    }

    return bf;
  }

  /**
   * 
   */
  private static BufferedImage edgeThinning(BufferedImage image) {
//    BufferedImage bf = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//    
    
    boolean oddIteration = true;
    boolean pixelsRemoved;
    
    do {
      pixelsRemoved = false;
    
      for (int x = 1; x < image.getWidth() - 1; x++) {
        for (int y = 1; y < image.getHeight() - 1; y++) {
          boolean replace = false;
          int p1 = grayscale(image.getRGB(x, y)) == 0 ? 1 : 0;
          
          // Black pixels are skipped
          if (p1 == 1)
            continue;
          
          int p2 = grayscale(image.getRGB(x, y - 1)) == 0 ? 1 : 0;
          int p3 = grayscale(image.getRGB(x + 1, y - 1)) == 0 ? 1 : 0;
          int p4 = grayscale(image.getRGB(x + 1, y)) == 0 ? 1 : 0;
          int p5 = grayscale(image.getRGB(x + 1, y + 1)) == 0 ? 1 : 0;
          int p6 = grayscale(image.getRGB(x, y + 1)) == 0 ? 1 : 0;
          int p7 = grayscale(image.getRGB(x - 1, y + 1)) == 0 ? 1 : 0;
          int p8 = grayscale(image.getRGB(x - 1, y)) == 0 ? 1 : 0;
          int p9 = grayscale(image.getRGB(x - 1, y - 1)) == 0 ? 1 : 0;
          
          int a = 
              (p2 - p3 == -1 ? 1 : 0) +
              (p3 - p4 == -1 ? 1 : 0) +
              (p4 - p5 == -1 ? 1 : 0) +
              (p5 - p6 == -1 ? 1 : 0) +
              (p6 - p7 == -1 ? 1 : 0) +
              (p7 - p8 == -1 ? 1 : 0) +
              (p8 - p9 == -1 ? 1 : 0) +
              (p9 - p2 == -1 ? 1 : 0);
          int b = p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
          
          // 2 <= b <= 6
          if ((b < 2) || (b > 6))
            continue;
          
          if (a == 1) {
            if (oddIteration) {
              replace = 
                  ((p2 * p4 * p6 == 0) && (p4 * p6 * p8 == 0));
            } else {
              replace = 
                  ((p2 * p4 * p8 == 0) && (p2 * p6 * p8 == 0));
            }
          } 
//          else if (a == 2) {
//            if (oddIteration) {
//              replace = 
//                  ((p4 * p6 == 1) && (p9 == 0) && (p4 * p2 == 1) && (p3 * p7 * p8 == 1));
//            } else {
//              replace = 
//                  ((p2 * p8 == 1) && (p5 == 0) && (p6 * p8 == 1) && (p3 * p4 * p7 == 1));
//            }
//          }
          
          if (replace) {
            image.getRaster().setPixel(x, y, new int[] { 0 });
            pixelsRemoved = true;
          }
        }
      }
      oddIteration = !oddIteration;
    } while (pixelsRemoved);

    return image;
  }
  
  
  /**
   * 
   */
  public static BufferedImage otsuThreshold(BufferedImage image, boolean thinning) {
    int pixelCount = image.getWidth() * image.getHeight();
    int[] histogram = histogramGS(image);
    
    int bestThreshold = 0;
    double sumB = 0;
    double wB = 0;
    double maximum = 0.0;
    double sum1 = 0;
    
    for (int i = 0; i < histogram.length; i++)
      sum1 += i * histogram[i];
    
    for (int threshold = 0; threshold < histogram.length; threshold++) {
      wB += histogram[threshold];
      
      if (wB == 0)
        continue;
      
      double wF = pixelCount - wB;
      
      if (wF == 0)
        break;
      
      sumB += threshold * histogram[threshold];
      double mB = sumB / wB;
      double mF = (sum1 - sumB) / wF;
      double between = wB * wF * (mB - mF) * (mB - mF);
      if (between >= maximum) {
        bestThreshold = threshold;
        maximum = between;
      }
    }
    
    BufferedImage bf = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
    
    for (int x = 0; x < bf.getWidth(); x++) {
      for (int y = 0; y < bf.getHeight(); y++) {
        int pixelVal = grayscale(image.getRGB(x, y)) > bestThreshold ? 255 : 0;
        
        bf.getRaster().setPixel(x, y, new int[] { pixelVal });
      }
    }

    if (thinning)
      bf = edgeThinning(bf);
      
    return bf;
  }
  
  
  /**
   * Grayscale gradient-based edge detection.
   */
  public static int[][] hough(BufferedImage image, double degreeAccuracy) {
    int thetaCount = (int) (360 / degreeAccuracy);
    double maxRho = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
    int rhoCount = (int) (maxRho * 2);
    int[][] houghTable = new int[rhoCount][thetaCount];
    
    double minr = 0;
    double maxr = 0;
    
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        if (grayscale(image.getRGB(x, y)) == 255) {
          for (int theta = 0; theta < thetaCount; theta++) {
            double angle = Math.toRadians(theta * degreeAccuracy);
            double rho = x * Math.cos(angle) + y * Math.sin(angle);
            
            if (rho < minr)
              minr = rho;
            if (rho > maxr)
              maxr = rho;
            
            int roundedRho = (int) (rho + maxRho);
            
            houghTable[roundedRho][theta]++;
            
            //System.out.println(roundedRho + " - " + rho);
          }
        }
      }
    }

    System.out.println("min " + minr);
    System.out.println("max " + maxr);
    
    System.out.println("dim " + image.getWidth() + " x " + image.getHeight());
    
    return houghTable;
  }
  
  public static List<HoughVote> houghTop(int[][] houghTable, int count, int voteThreshold) {
    List<HoughVote> votes = new ArrayList<HoughVote>();
    for (int rho = 0; rho < houghTable.length; rho++) {
      for (int angle = 0; angle < houghTable[rho].length; angle++) {
        if (houghTable[rho][angle] > voteThreshold) {
          // TODO: proper rho and angle?
          votes.add(new HoughVote(rho, angle, houghTable[rho][angle]));
        }
      }
    }
    
    Collections.sort(votes, new Comparator<HoughVote>() {
      public int compare(HoughVote arg0, HoughVote arg1) {
        return arg1.getVotes() - arg0.getVotes();
      }
    });
    
    return votes.subList(0, count);
  }
  
}
