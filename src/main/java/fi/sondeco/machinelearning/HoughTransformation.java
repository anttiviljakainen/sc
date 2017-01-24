package fi.sondeco.machinelearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HoughTransformation {

  public HoughTransformation(int imageWidth, int imageHeight, 
      double angleThreshold, int[][] houghTable) {
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
    this.angleThreshold = angleThreshold;
    this.houghTable = houghTable;
  }
  
  public int getImageWidth() {
    return imageWidth;
  }
  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }

  public int[][] getHoughTable() {
    return houghTable;
  }

  public void setHoughTable(int[][] houghTable) {
    this.houghTable = houghTable;
  }

  public List<HoughVote> getVotes(int count, int voteThreshold) {
//    int maxRho = (int) Math.ceil((Math.sqrt(2.0) * Math.max(getImageHeight(), getImageWidth())) / 2.0);
    int maxRho = (int) Math.ceil(Math.sqrt(getImageWidth() * getImageWidth() + getImageHeight() * getImageHeight()));
    
    List<HoughVote> votes = new ArrayList<HoughVote>();
    for (int rho = 0; rho < houghTable.length; rho++) {
      for (int angle = 0; angle < houghTable[rho].length; angle++) {
        if (houghTable[rho][angle] > voteThreshold) {
          votes.add(new HoughVote(rho - maxRho, angle * angleThreshold, houghTable[rho][angle]));
        }
      }
    }
    
    Collections.sort(votes, new Comparator<HoughVote>() {
      public int compare(HoughVote arg0, HoughVote arg1) {
        return arg1.getVotes() - arg0.getVotes();
      }
    });
    
    return votes.subList(0, Math.min(count, votes.size()));
  }
  
  public double getAngleThreshold() {
    return angleThreshold;
  }

  public void setAngleThreshold(double angleThreshold) {
    this.angleThreshold = angleThreshold;
  }

  private int imageWidth;
  private int imageHeight;
  private int[][] houghTable;
  private double angleThreshold;
}
