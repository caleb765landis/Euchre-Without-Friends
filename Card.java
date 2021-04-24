// Card.java

import java.util.*;

public class Card {
  protected String name;
  protected String suit;
  protected int rank;
  protected boolean trump;

  public Card(String suit, int rank) {
    this.suit = suit;
    this.rank = rank;
    this.trump = false;
  } // end constructor

  public void initName() {
    if (rank == 3) {
      this.name = "Ace of " + this.getSuit();
    } else if (rank == 4) {
      this.name = "King of " + this.getSuit();
    } else if (rank == 5) {
      this.name = "Queen of " + this.getSuit();
    } else if (rank == 6) {
      this.name = "Jack of " + this.getSuit();
    } else if (rank == 7) {
      this.name = "Ten of " + this.getSuit();
    } else if (rank == 8) {
      this.name = "Nine of " + this.getSuit();
    } // end if
  } // end initName()

  public String getName() {
    return this.name;
  } // end getName()

  public String getSuit() {
    return this.suit;
  } // end getSuit()

  public void setRank(int rank) {
    this.rank = rank;
  } // end setRank()

  public int getRank() {
    return this.rank;
  } // end getRank()

  public void setTrump(boolean trump) {
    this.trump = trump;
  } // end setTrump()

  public boolean getTrump() {
    return this.trump;
  } // end getTrump()

  // tests Card class
  public static void main(String[] args) {
    ArrayList<Card> cards = new ArrayList<>();
    String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

    for (int i = 0; i < 4; i++) {
      String currentSuit = suits[i];
      for (int j = 3; j < 9; j++) {
        cards.add( new Card(currentSuit, j) );
      } // end for
    } // end for

    for (int k = 0; k < 24; k++) {
      Card currentCard = cards.get(k);
      currentCard.initName();
      System.out.println(currentCard.getName());
    } // end for
  } // end public static void main(String[] args)
} // end Card.java
