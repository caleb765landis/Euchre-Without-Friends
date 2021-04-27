// TeamMember.java
// abstract class to be extended by Player.java and AI.java 

import java.util.*;

public abstract class TeamMember {
  protected Hand hand;
  protected boolean dealer;
  protected boolean alone;
  protected String name;
  protected boolean choseTrump;

  // constructor
  public TeamMember(){}

  // prints out name of each card in hand with options for user to select card
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

  public void setTrumpDecision(boolean decision) {
    this.choseTrump = decision;
  } // end setTrumpDecision()

  public boolean getTrumpDecision() {
    return this.choseTrump;
  } // end getTrumpDecision()

  // abstract methods to be implemented in Player.java and AI.java
  public abstract String order(Card turnedUp);
  public abstract String name(String turnedOver);
  public abstract void discard(Card turnedUp);
  public abstract boolean alone(String trump);
  public abstract Card play(String currentSuit);
} // end TeamMember.java
