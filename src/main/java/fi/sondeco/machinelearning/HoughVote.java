package fi.sondeco.machinelearning;

public class HoughVote {
  
  public HoughVote(int rho, int angle, int votes) {
    this.rho = rho;
    this.angle = angle;
    this.votes = votes;
    
  }
  
  public int getRho() {
    return rho;
  }

  public int getAngle() {
    return angle;
  }

  public int getVotes() {
    return votes;
  }

  private final int rho;
  private final int angle;
  private final int votes;
}
