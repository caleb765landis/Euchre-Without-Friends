// Player.java

import java.util.*;

public class Player extends TeamMember {
  private Hand hand;
  private boolean dealer;
  private boolean alone;

  public Player(Deck deck) {
    this.hand = new Hand();
    this.hand.dealHand(deck);
    this.dealer = false;
    this.alone = false;
  } // end constructor

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

  // tests Player class 
  public static void main(String[] args) {
    Deck deck = new Deck();
    Player p1 = new Player(deck);
    Player p2 = new Player(deck);

    System.out.println("P1:");
    p1.hand.getHand();
    System.out.println("Dealer: " + p1.getDealer());
    p1.setDealer(true);
    System.out.println("Dealer after setDealer(true): " + p1.getDealer());
    System.out.println("Alone: " + p1.getAlone());
    p1.setAlone(true);
    System.out.println("Alone after setAlone(true): " + p1.getAlone());
    System.out.println();

    System.out.println("P2:");
    p2.hand.getHand();
    System.out.println("Dealer: " + p2.getDealer());
    p2.setDealer(true);
    System.out.println("Dealer after setDealer(true): " + p2.getDealer());
    System.out.println("Alone: " + p2.getAlone());
    p2.setAlone(true);
    System.out.println("Alone after setAlone(true): " + p2.getAlone());
    System.out.println();
    //p2.hand.getHand();
  } // end public static void main(String[] args)
} // end Player.java
