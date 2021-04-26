// TeamMember.java
// interface to be extended by Player.java

import java.util.*;

public abstract class TeamMember {
  protected Hand hand;
  protected boolean dealer;
  protected boolean alone;
  protected String name;

  // constructor
  public TeamMember(){}

  public void setDealer(boolean dealer) {
    this.dealer = dealer;
  } // end setDealer()

  public boolean getDealer() {
    return this.dealer;
  } // end getDealer()

  public void setAlone(boolean alone) {
    this.alone = alone;
  } // end setAlone()

  public boolean getAlone() {
    return this.alone;
  } // end getAlone()

  public String getName() {
    return this.name;
  } // end getName()

  // abstract methods to be implemented in Player.java
  public abstract String order(Card turnedUp);
  /*
  public abstract void setDealer(boolean dealer);
  public abstract boolean getDealer();
  public abstract void setAlone(boolean alone);
  public abstract boolean getAlone();
  public abstract String getName();
  */
} // end TeamMember.java
