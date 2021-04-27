// Hand.java

import java.util.*;

public class Hand {
  protected ArrayList<Card> hand;

  // constructor
  public Hand() {
      hand = new ArrayList<Card>();
  } // end constructor

  // prints out name of each card in hand
  public void getHand() {
    for (int i = 0; i < this.getHandSize(); i++) {
      System.out.println("  " + this.hand.get(i).getName());
    } // end for
  } // end getHand()

  // takes in Card card as parameter
  // card will be random card from deck
  // used in dealerDiscard()
  public void addCard(Card card) {
    this.hand.add(card);
  } // end addCard()

  // takes int i as parameter
  // i is player's selection on which card to play in hand
  // also used in dealerDiscard()
  public void removeCard(int i) {
    Card currentCard = this.getCard(i);
    this.hand.remove(currentCard);
  } // end removeCard()

  public Card getCard(int i) {
    return this.hand.get(i);
  } // end getCard()

  public int getHandSize() {
    return this.hand.size();
  } // end getHandSize()

  // gets frequency of whatever card's suit is being passed
  public int getSuitFrequency(Card card) {
    String cardSuit = card.getSuit();
    // create an array of each card in hand's suit
    ArrayList<String> handSuits = new ArrayList<String>();
    for (int i = 0; i < this.hand.size(); i++) {
      Card currentCard = this.hand.get(i);
      handSuits.add(currentCard.getSuit());
    } // end for
    // get and return the frequency of how many times card's suit shows up in array
    int frequency = Collections.frequency(handSuits, cardSuit);
    return frequency;
  } // end getSuitFrequency()

  // gets frequency of how many cards in player's hand are trump
  // used for AI decisions 
  public int getTrumpFrequency(String trump) {
    ArrayList<String> handSuits = new ArrayList<String>();
    for (int i = 0; i < this.hand.size(); i++) {
      Card currentCard = this.hand.get(i);
      handSuits.add(currentCard.getSuit());
    } // end for
    int frequency = Collections.frequency(handSuits, trump);
    return frequency;
  } // end getTrumpFrequency

  /*
  get a random card in the deck
  add that card to hand
  remove that card from deck
  do this five times
  */
  public void dealHand(Deck deck) {
    for (int i = 0; i < 5; i++) {
      // (int) ((Math.random() * (max - min)) + min) [min, max)
      int randNum = (int) (Math.random() * (deck.getDeckSize()));
      Card currentCard = deck.getCard(randNum);
      this.addCard(currentCard);
      deck.removeCard(randNum);
    } // end for
  } // end dealHand()

  // bubble sort
  // puts card with rank of lowest number (1 being the lowest possible) at front of ArrayList
  public void sortHand() {
    for (int i = 0; i < (this.hand.size() - 1); i++) {
      for (int j = 0; j < (this.hand.size() - 1); j++) {
        if (this.hand.get(j).getRank() > (this.hand.get(j + 1).getRank())) {
          Collections.swap(this.hand, j, j + 1);
        } // end swap if
      } // end j for
    } // end i for
  } // end sortHand()

  // tests Hand class
  public static void main(String[] args) {
    Deck deck = new Deck();

    Hand p1 = new Hand();
    Hand p2 = new Hand();
    p1.dealHand(deck);
    p2.dealHand(deck);

    // should print out 5 cards
    System.out.println("p1 hand:");
    p1.getHand();
    System.out.println();

    // should print out 5 cards
    System.out.println("p2 hand:");
    p2.getHand();
    System.out.println();

    // removes first card from player's hand
    // should print out 4 cards
    System.out.println("p1 hand w first card removed:");
    p1.removeCard(0);
    p1.getHand();
    System.out.println();

    // should print out 5 cards still
    System.out.println("p2 hand before sort:");
    p2.getHand();
    System.out.println();

    System.out.println("p2 hand after sort:");
    p2.sortHand();
    p2.getHand();
  } // end public static void main(String[] args)
} // end Hand.java
