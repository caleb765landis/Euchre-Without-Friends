// TeamMember.java
// interface to be extended by Player.java

public abstract class TeamMember {

  // constructor
  public TeamMember(){}

  // abstract methods to be implemented in Player.java
  public abstract void setDealer(boolean dealer);
  public abstract boolean getDealer();
  public abstract void setAlone(boolean alone);
  public abstract boolean getAlone();
} // end TeamMember.java
