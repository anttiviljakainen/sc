package fi.sondeco.machinelearning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.sondeco.matrix.Matrix;

public class ImageUtilsTestApp {

  private JFrame frame;
  private JPanel imagePanel;
  private JPanel buttonPanel;
  private ImageIcon imageIcon;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ImageUtilsTestApp window = new ImageUtilsTestApp();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   * @throws IOException 
   */
  public ImageUtilsTestApp() throws IOException {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   * @throws IOException 
   */
  private void initialize() throws IOException {
    frame = new JFrame();
    frame.setBounds(100, 100, 600, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    frame.setLayout(new BorderLayout(5, 5));
    frame.add(imagePanel = new JPanel(), BorderLayout.CENTER);
    frame.add(buttonPanel = new JPanel(), BorderLayout.EAST);
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

    JButton defaultImgBtn = new JButton("Default Image");
    defaultImgBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          imageIcon.setImage(defaultImage);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(defaultImgBtn);
    
    JButton edgeDetectBtn = new JButton("Edge detection");
    edgeDetectBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          
          BufferedImage edgeDetectGS = ImageUtils.gradientEdgeDetectGS(defaultImage);
          
          imageIcon.setImage(edgeDetectGS);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(edgeDetectBtn);

    JButton edgeDetectOtsuBtn = new JButton("Edge detection & otsu");
    edgeDetectOtsuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          
          BufferedImage edgeDetectGS = ImageUtils.gradientEdgeDetectGS(defaultImage);
          BufferedImage image = ImageUtils.otsuThreshold(edgeDetectGS, false);
          
          imageIcon.setImage(image);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(edgeDetectOtsuBtn);

    JButton otsuBtn = new JButton("Otsu");
    otsuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          
          BufferedImage image = ImageUtils.otsuThreshold(defaultImage, false);
          
          imageIcon.setImage(image);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(otsuBtn);
    
    JButton edgeDetectOtsuThinBtn = new JButton("Edge detection & otsu & thinning");
    edgeDetectOtsuThinBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          
          BufferedImage edgeDetectGS = ImageUtils.gradientEdgeDetectGS(defaultImage);
          BufferedImage image = ImageUtils.otsuThreshold(edgeDetectGS, true);
          
          imageIcon.setImage(image);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(edgeDetectOtsuThinBtn);

    JButton otsuthinBtn = new JButton("Otsu + thinning");
    otsuthinBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          
          BufferedImage image = ImageUtils.otsuThreshold(defaultImage, true);
          
          imageIcon.setImage(image);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(otsuthinBtn);

    JButton hough = new JButton("Hough");
    hough.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          BufferedImage defaultImage = loadDefaultImage();
          
          boolean thinning = true;
          BufferedImage edgeDetectGS = ImageUtils.gradientEdgeDetectGS(defaultImage);
          BufferedImage image = ImageUtils.otsuThreshold(edgeDetectGS, thinning);
          
          double degreeAccuracy = 5;
          int[][] houghTable = ImageUtils.hough(image, degreeAccuracy);
          
          final int topLines = 100;
          final List<HoughVote> houghTop = ImageUtils.houghTop(houghTable, topLines, 1);
          for (HoughVote vote : houghTop)
            System.out.println(String.format("Hou (%d, %d, %d)", vote.getRho(), vote.getAngle(), vote.getVotes()));
          
          image = loadDefaultImage();

          final BitSetCostFunction costf = new BitSetCostFunction() {
            public double cost(BitSet bitset) {
              int dimlen = topLines;
              BitSet vertical = bitset.get(0, dimlen);
              BitSet horizontal = bitset.get(dimlen, dimlen * 2);
              
              // Point where lines cross
              // Orthogonality
              
              double parallelerror = 0;
              double spreadpenalty = 0;
              double angleerror = 0;
              double verticalcardinalitypenalty = Math.abs((8 - vertical.cardinality()) * 100000);
              double horizontalcardinalitypenalty = Math.abs((8 - horizontal.cardinality()) * 100000);
              Matrix rhov = new Matrix(vertical.cardinality(), 1);
              Matrix rhoh = new Matrix(horizontal.cardinality(), 1);
              
              int r = 0;
              for (int i = vertical.nextSetBit(0); i >= 0; i = vertical.nextSetBit(i + 1)) {
                for (int j = vertical.nextSetBit(0); j >= 0; j = vertical.nextSetBit(j + 1)) {
                  double err = houghTop.get(i).getAngle() - houghTop.get(j).getAngle();
                  
                  parallelerror += err * err;
                }

                double err = 90 - houghTop.get(i).getAngle();
                angleerror += err * err;
                rhov.set(r, 0, houghTop.get(i).getRho());
                r++;
              }
              
              r = 0;
              for (int i = horizontal.nextSetBit(0); i >= 0; i = horizontal.nextSetBit(i + 1)) {
                for (int j = horizontal.nextSetBit(0); j >= 0; j = horizontal.nextSetBit(j + 1)) {
                  double err = houghTop.get(i).getAngle() - houghTop.get(j).getAngle();
                  
                  parallelerror += err * err;
                }

                double err = houghTop.get(i).getAngle();
                angleerror += err * err;
                rhoh.set(r, 0, houghTop.get(i).getRho());
                r++;
              }

              // Average distance
              
              rhov.sort();
              rhoh.sort();
              
              Matrix rhovdist = rhov.sdist();
              Matrix rhohdist = rhoh.sdist();

              double rhovmeandist = rhovdist.mean();
              double rhohmeandist = rhohdist.mean();

              rhovdist.subtract(rhovmeandist).dotpower(2);
              rhohdist.subtract(rhohmeandist).dotpower(2);
              
              spreadpenalty = rhovdist.sum() + rhohdist.sum();
              
              double totalcost = 
                  angleerror * 10 +
                  spreadpenalty +
                  parallelerror +
                  verticalcardinalitypenalty + 
                  horizontalcardinalitypenalty;
              
              System.out.println(String.format(
                  "Cost: Total %d, Parallel: %d, Spread: %d, angle: %d, VPen: %d, HPen: %d", 
                  (int) totalcost, 
                  (int) parallelerror, 
                  (int) spreadpenalty, 
                  (int) angleerror,
                  (int) verticalcardinalitypenalty, 
                  (int) horizontalcardinalitypenalty));
              
              return totalcost;
            }
          };
          
          GridOptimizer opt = new GridOptimizer(20, topLines * 2, 4000, 0, 0.01, costf);
          List<BitSet> minimize = opt.minimize();
          for (BitSet bs : minimize) {
            System.out.println(String.format("Cardinality: %d, total cost: %f", bs.cardinality(), costf.cost(bs)));
          }
          
          BitSet min = Collections.min(minimize, new Comparator<BitSet>() {
            public int compare(BitSet o1, BitSet o2) {
              return (int) Math.signum(costf.cost(o1) - costf.cost(o2));
            }
          });
          
          System.out.println("Best");
          System.out.println(String.format("Cardinality: %d, total cost: %f", min.cardinality(), costf.cost(min)));
          
          List<HoughVote> houghTop2 = new ArrayList<HoughVote>();
          List<HoughVote> houghTop2v = new ArrayList<HoughVote>();
          List<HoughVote> houghTop2h = new ArrayList<HoughVote>();

          for (int i = min.nextSetBit(0); i >= 0; i = min.nextSetBit(i + 1)) {
            houghTop2.add(houghTop.get(i % topLines));
          }
          
          BitSet vertical = min.get(0, topLines);
          for (int i = vertical.nextSetBit(0); i >= 0; i = vertical.nextSetBit(i + 1)) {
            houghTop2v.add(houghTop.get(i));
//            System.out.println(houghTop.get(i).getRho());
          }
          BitSet horizontal = min.get(topLines, topLines * 2);
          for (int i = horizontal.nextSetBit(0); i >= 0; i = horizontal.nextSetBit(i + 1)) {
            houghTop2h.add(houghTop.get(i));
//            System.out.println(houghTop.get(i).getRho());
          }

          for (HoughVote vote : houghTop2v) {
            double maxRho = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
            double rho = vote.getRho() - maxRho;
            double angle = Math.toRadians(vote.getAngle() * degreeAccuracy);
            
            int x1 = (int) (Math.cos(angle) * rho);
            int y1 = (int) (Math.sin(angle) * rho);
            int x2 = (int) (Math.cos(angle) * rho * 2);
            int y2 = (int) (Math.sin(angle) * rho * 2);
                
            Graphics g = image.getGraphics();
            g.setColor(Color.red);
            if ((x1 > 0) && (y1 > 0)) {
            }
                
            double m = -(Math.cos(angle) / Math.sin(angle)); 
            double b = rho / Math.sin(angle);
            
            x1 = 0;
            x2 = image.getWidth();
            y1 = (int) b;
            y2 = (int) (m * image.getWidth() + b);
                
            g.setColor(Color.red);
            int xa = -500;
            int xb = 500;
                
            x1 = (int) (xa * Math.cos(angle) - rho * Math.sin(angle));
            y1 = (int) (xa * Math.sin(angle) + rho * Math.cos(angle));
            x2 = (int) (xb * Math.cos(angle) - rho * Math.sin(angle));
            y2 = (int) (xb * Math.sin(angle) + rho * Math.cos(angle));
                
    
            double a0 = Math.cos(angle);
            double b0 = Math.sin(angle);
            double x0 = a0 * rho;
            double y0 = b0 * rho;
            
            x1 = (int) (x0 + 1000 * (-b0));
            y1 = (int) (y0 + 1000 * (a0));
            x2 = (int) (x0 - 1000 * (-b0));
            y2 = (int) (y0 - 1000 * (a0));
            
            g.setColor(Color.orange);
            g.drawLine(x1, y1, x2, y2);
    //        System.out.println(String.format("Angle: %f, y1: %d, y2: %d, x1: %d, x2: %d", 
    //            vote.getAngle() * degreeAccuracy, y1, y2, x1, x2));
          }

          for (HoughVote vote : houghTop2h) {
                //System.out.println(String.format("i: %d, j: %d, n: %d", i, j, houghTable[i][j]));
                
            double maxRho = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
            double rho = vote.getRho() - maxRho;
            double angle = Math.toRadians(vote.getAngle() * degreeAccuracy);
            
            int x1 = (int) (Math.cos(angle) * rho);
            int y1 = (int) (Math.sin(angle) * rho);
            int x2 = (int) (Math.cos(angle) * rho * 2);
            int y2 = (int) (Math.sin(angle) * rho * 2);
                
//            System.out.println(String.format("j: %d, angle: %f, x: %f, y: %f, x1: %d, y1: %d", 
//                vote.getAngle(), angle, Math.cos(angle), Math.sin(angle), x1, y1));

            Graphics g = image.getGraphics();
            g.setColor(Color.red);
            if ((x1 > 0) && (y1 > 0)) {
//                  image.setRGB(x1, y1, Color.green.getRGB());
//                  image.setRGB(y1, x1, Color.green.getRGB());
            }
                
            double m = -(Math.cos(angle) / Math.sin(angle)); 
            double b = rho / Math.sin(angle);
            
            x1 = 0;
            x2 = image.getWidth();
            y1 = (int) b;
            y2 = (int) (m * image.getWidth() + b);
                
//                System.out.println(String.format("Angle: %f, y1: %d, y2: %d", 
//                    j * degreeAccuracy, y1, y2));
            g.setColor(Color.red);
//                g.drawLine(x1, y1, x2, y2);
                
//                x' = x \cos \theta - y \sin \theta\,,
//                y' = x \sin \theta + y \cos \theta\,.
                
//                int y = rho;
            int xa = -500;
            int xb = 500;
                
//                rho = Math.abs(rho);
                
            x1 = (int) (xa * Math.cos(angle) - rho * Math.sin(angle));
            y1 = (int) (xa * Math.sin(angle) + rho * Math.cos(angle));
            x2 = (int) (xb * Math.cos(angle) - rho * Math.sin(angle));
            y2 = (int) (xb * Math.sin(angle) + rho * Math.cos(angle));
                
//                g.setColor(Color.green);
//                g.drawLine(x1, y1, x2, y2);
//                System.out.println(String.format("Angle: %f, y1: %d, y2: %d, x1: %d, x2: %d", 
//                    j * degreeAccuracy, y1, y2, x1, x2));

            double a0 = Math.cos(angle);
            double b0 = Math.sin(angle);
            double x0 = a0 * rho;
            double y0 = b0 * rho;
            
            x1 = (int) (x0 + 1000 * (-b0));
            y1 = (int) (y0 + 1000 * (a0));
            x2 = (int) (x0 - 1000 * (-b0));
            y2 = (int) (y0 - 1000 * (a0));
            
            g.setColor(Color.green);
            g.drawLine(x1, y1, x2, y2);
//            System.out.println(String.format("Angle: %f, y1: %d, y2: %d, x1: %d, x2: %d", 
//                vote.getAngle() * degreeAccuracy, y1, y2, x1, x2));
          }
          
//          for (int i = 0; i < houghTable.length; i++) {
//            for (int j = 0; j < houghTable[i].length; j++) {
//              if (houghTable[i][j] > 180) {
//                //System.out.println(String.format("i: %d, j: %d, n: %d", i, j, houghTable[i][j]));
//                
//                double maxRho = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
//                double rho = i - maxRho;
//                double angle = Math.toRadians(j * degreeAccuracy);
//                
//                int x1 = (int) (Math.cos(angle) * rho);
//                int y1 = (int) (Math.sin(angle) * rho);
//                int x2 = (int) (Math.cos(angle) * rho * 2);
//                int y2 = (int) (Math.sin(angle) * rho * 2);
//                
//                System.out.println(String.format("j: %d, angle: %f, x: %f, y: %f, x1: %d, y1: %d", 
//                    j, angle, Math.cos(angle), Math.sin(angle), x1, y1));
//
//                Graphics g = image.getGraphics();
//                g.setColor(Color.red);
//                if ((x1 > 0) && (y1 > 0)) {
////                  image.setRGB(x1, y1, Color.green.getRGB());
////                  image.setRGB(y1, x1, Color.green.getRGB());
//                }
//                
//                double m = -(Math.cos(angle) / Math.sin(angle)); 
//                double b = rho / Math.sin(angle);
//                
//                x1 = 0;
//                x2 = image.getWidth();
//                y1 = (int) b;
//                y2 = (int) (m * image.getWidth() + b);
//                
////                System.out.println(String.format("Angle: %f, y1: %d, y2: %d", 
////                    j * degreeAccuracy, y1, y2));
//                g.setColor(Color.red);
////                g.drawLine(x1, y1, x2, y2);
//                
////                x' = x \cos \theta - y \sin \theta\,,
////                y' = x \sin \theta + y \cos \theta\,.
//                
////                int y = rho;
//                int xa = -500;
//                int xb = 500;
//                
////                rho = Math.abs(rho);
//                
//                x1 = (int) (xa * Math.cos(angle) - rho * Math.sin(angle));
//                y1 = (int) (xa * Math.sin(angle) + rho * Math.cos(angle));
//                x2 = (int) (xb * Math.cos(angle) - rho * Math.sin(angle));
//                y2 = (int) (xb * Math.sin(angle) + rho * Math.cos(angle));
//                
////                g.setColor(Color.green);
////                g.drawLine(x1, y1, x2, y2);
////                System.out.println(String.format("Angle: %f, y1: %d, y2: %d, x1: %d, x2: %d", 
////                    j * degreeAccuracy, y1, y2, x1, x2));
//
//                double a0 = Math.cos(angle);
//                double b0 = Math.sin(angle);
//                double x0 = a0 * rho;
//                double y0 = b0 * rho;
//                
//                x1 = (int) (x0 + 1000 * (-b0));
//                y1 = (int) (y0 + 1000 * (a0));
//                x2 = (int) (x0 - 1000 * (-b0));
//                y2 = (int) (y0 - 1000 * (a0));
//                
//                g.setColor(Color.green);
//                g.drawLine(x1, y1, x2, y2);
//                System.out.println(String.format("Angle: %f, y1: %d, y2: %d, x1: %d, x2: %d", 
//                    j * degreeAccuracy, y1, y2, x1, x2));
//              }
//            }
//          }
          
          imageIcon.setImage(image);
          imagePanel.repaint();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    buttonPanel.add(hough);

    imagePanel.add(new JLabel(imageIcon = new ImageIcon()));
    viewDefaultImage();
  }
  
  private BufferedImage loadDefaultImage() throws IOException {
    return ImageIO.read(this.getClass().getResource("4.jpg"));
  }
  
  private void viewDefaultImage() throws IOException {
    imageIcon.setImage(loadDefaultImage());
  }

  
  
}
