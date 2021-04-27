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

  public void getPlayerHand() {
    for (int i = 0; i < this.hand.getHandSize(); i++) {
      System.out.println("  " + i + ") " + this.hand.getCard(i).getName());
    } // end for
  } // end getPlayerHand

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
  public abstract String name(String turnedOver);
  public abstract void discard(Card turnedUp);
} // end TeamMember.java
