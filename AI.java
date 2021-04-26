// AI.java

import java.util.*;

public class AI extends TeamMember {

  public AI(Deck deck, String name) {
    this.hand = new Hand();
    this.hand.dealHand(deck);
    this.hand.sortHand();
    this.dealer = false;
    this.alone = false;
    this.name = name;
  } // end constructor

  public String order(Card turnedUp) {
    int numSameSuit = this.hand.getSuitFrequency(turnedUp);
    String decision;
    // if hand has 3 or more of same suit, order up
    // otherwise pass
    if (numSameSuit > 2) {
      decision = "order";
    } else {
      decision = "passing";
    } // end if
    return decision;
  } // end order()

  // tests AI class
  public static void main(String[] args) {
    Deck deck = new Deck();
    AI ai = new AI(deck, "ai");
    System.out.println("ai hand after sort:");
    ai.hand.getHand();

    // tests order()
    int randNum = (int) (Math.random() * (deck.getDeckSize()));
    Card turnedUp = deck.getCard(randNum);
    System.out.println(turnedUp.getSuit());
    System.out.println(ai.order(turnedUp));
  }
} // end AI.java
