package fi.sondeco.machinelearning;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    frame.setBounds(100, 100, 450, 300);
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
          
          BufferedImage edgeDetectGS = ImageUtils.gradientEdgeDetectGS(defaultImage);
          BufferedImage image = ImageUtils.otsuThreshold(edgeDetectGS, false);
          
          ImageUtils.hough(image, 5);
          
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
