// Deck.java

import java.util.*;

public class Deck {
  protected static ArrayList<Card> deck= new ArrayList<>();
  protected String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

  public Deck() {
    this.setDeck();
  } // end constructor

  public void setDeck() {
    this.deck.clear();

    for (int i = 0; i < 4; i++) {
      String currentSuit = suits[i];
      for (int j = 3; j < 9; j++) {
        this.deck.add( new Card(currentSuit, j) );
      } // end for
    } // end for

    for (int k = 0; k < 24; k++) {
      Card currentCard = this.deck.get(k);
      currentCard.initName();
    } // end for
  } // end setDeck()

  /* used when testing ArrayList with methods from different classes
  public ArrayList<Card> getDeck() {
    return this.deck;
  } // end getDeck()
  */

  public Card getCard(int i) {
    return this.deck.get(i);
  } // end getCard()

  // takes int i as parameter
  // i will be random integer from dealHand()
  public void removeCard(int i) {
    //Card currentCard = this.getCard(i);
    //this.deck.remove(currentCard);
    this.deck.remove(i);
  } // end removeCard()

  public int getDeckSize() {
    return this.deck.size();
  } // end getDeckSize()

  // tests Deck class
  public static void main(String[] args) {
    Deck deck = new Deck();

    // prints deck
    for (int i = 0; i < 24; i++) {
      System.out.println(deck.getCard(i).getName());
    } // end for

    System.out.println();

    // removes all cards but first one
    // leaves first card to make sure clear() works when
      // calling setDeck() again
    for (int j = 23; j > 0; j--) {
      deck.removeCard(j);
    } // end for

    // makes sure removeCard() worked
    // should only print "Ace of Hearts"
    for (int k = 0; k < deck.getDeckSize(); k++) {
      System.out.println(deck.getCard(k).getName());
    } // end for

    System.out.println();

    deck.setDeck();

    // should print out only 24 cards
    for (int l = 0; l < deck.getDeckSize(); l++) {
      System.out.println(deck.getCard(l).getName());
    } // end for
  } // end public static void main(String[] args)
} // end Deck.java
