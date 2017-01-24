package fi.sondeco.machinelearning;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Test;

public class ImageUtilsTests {

  @Test
  public void testLinesIntersection1() {
    Point2D p1 = new Point2D(1, 1);
    Point2D p2 = new Point2D(7, 7);
    Point2D p3 = new Point2D(2, 3);
    Point2D p4 = new Point2D(9, 4);

    Point2D p = ImageUtils.linesIntersection(p1, p2, p3, p4, true);
    assertEquals(3.1667, p.x, 0.001);
    assertEquals(3.1667, p.y, 0.001);
  }

  @Test
  public void testLinesIntersection2() {
    Point2D p1 = new Point2D(-3, -3);
    Point2D p2 = new Point2D(3, 3);
    Point2D p3 = new Point2D(-3, 3);
    Point2D p4 = new Point2D(3, -3);

    Point2D p = ImageUtils.linesIntersection(p1, p2, p3, p4, true);
    assertEquals(0, p.x, 0.001);
    assertEquals(0, p.y, 0.001);
  }

  @Test
  public void testLinesIntersectionParallel() {
    Point2D p1 = new Point2D(3, 3);
    Point2D p2 = new Point2D(6, 3);
    Point2D p3 = new Point2D(1, 1);
    Point2D p4 = new Point2D(5, 1);

    assertNull(ImageUtils.linesIntersection(p1, p2, p3, p4, true));
  }

  @Test
  public void testLinesIntersectionLines() {
    Point2D p1 = new Point2D(3, 3);
    Point2D p2 = new Point2D(6, 6);
    Point2D p3 = new Point2D(1, 1);
    Point2D p4 = new Point2D(5, 1);

    assertNotNull(ImageUtils.linesIntersection(p1, p2, p3, p4, false));
  }
  
  @Test
  public void testHoughTransform() {
    BufferedImage bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_BYTE_BINARY);
    
    Graphics g = bi.getGraphics();
    g.setColor(Color.black);
    g.fillRect(0, 0, 1000, 1000);
    g.setColor(Color.white);
    g.drawLine(10, 100, 30, 100);
    
    HoughTransformation hough = ImageUtils.hough(bi, 5);
    List<HoughVote> houghTop = hough.getVotes(1, 20);
    
    HoughVote houghVote = houghTop.get(0);
    double angle = houghVote.getAngle();
    assertTrue(angle == 90);
    assertEquals(100, houghVote.getRho());
  }
  
}
