package fi.sondeco.machinelearning;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import fi.sondeco.matrix.Matrix;

public class MachineLearningUtils {

  public static Matrix toGrayScaleMatrix(BufferedImage image) {
    Matrix m = new Matrix(image.getHeight(), image.getWidth());
    
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int rgb = image.getRGB(x, y);
        
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        
        m.set(y, x, (r + g + b) / 3);
      }
    }

    return m;
  }

  /**
   * Turns a matrix into grayscale image. Every cell of the matrix will be 
   * interpreted as a pixel in the resulting image.
   * 
   * Assumes matrix has values between 0 and 1.
   * 
   * @param m
   * @return
   */
  public static BufferedImage toImg(Matrix m) {
    BufferedImage bf = new BufferedImage(m.getColumns(), m.getRows(), BufferedImage.TYPE_BYTE_GRAY);
    
    for (int i = 0; i < m.getRows(); i++) {
      for (int j = 0; j < m.getColumns(); j++) {
        int pixel = (int) Math.floor(256d * m.get(i, j));
        bf.getRaster().setPixel(j, i, new int[] { pixel });
      }
    }

    return bf;
  }
  
  public static BufferedImage getScaledInstance(BufferedImage img,
      int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
    int type = (img.getTransparency() == Transparency.OPAQUE) ? 
        BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = (BufferedImage) img;
    int w, h;
    if (higherQuality) {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    } else {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do {
      if (higherQuality && w > targetWidth) {
        w /= 2;
        if (w < targetWidth) {
          w = targetWidth;
        }
      }

      if (higherQuality && h > targetHeight) {
        h /= 2;
        if (h < targetHeight) {
          h = targetHeight;
        }
      }

      BufferedImage tmp = new BufferedImage(w, h, type);
      Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      g2.drawImage(ret, 0, 0, w, h, null);
      g2.dispose();

      ret = tmp;
    } while (w != targetWidth || h != targetHeight);

    return ret;
  }

  public static void writeJPG(BufferedImage bufferedImage,
      OutputStream outputStream, float quality) throws IOException {
    Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter imageWriter = iterator.next();
    ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
    imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    imageWriteParam.setCompressionQuality(quality);
    ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(
        outputStream);
    imageWriter.setOutput(imageOutputStream);
    IIOImage iioimage = new IIOImage(bufferedImage, null, null);
    imageWriter.write(null, iioimage, imageWriteParam);
    imageOutputStream.flush();
  }
}
