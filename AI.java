// AI.java
// extends abstract TeamMember class
// AI are dumb to make the game easy to show off
// they still follow rules though

import java.util.*;

public class AI extends TeamMember {

  // constructor
  // takes in deck as parameter so player can be dealt hand
  // takes in name as parameter so referring to player is easier
  public AI(Deck deck, String name) {
    this.hand = new Hand();
    this.hand.dealHand(deck);
    this.hand.sortHand();
    this.dealer = false;
    this.alone = false;
    this.name = name;
    this.choseTrump = false;
  } // end constructor

  // handles AI decisions during order trump phase
  // returns "passing" or "ordering"
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

  // handles AI decisions during naming trump phase
  // returns either "passing" or suit string to be set as trump
  public String name(String turnedOver) {
    String decision;
    // ArrayList of all suits in AI's hand
    ArrayList<String> handSuits = new ArrayList<String>();
    for (int i = 0; i < 5; i++) {
      handSuits.add(this.hand.getCard(i).getSuit());
    } // end for

    // creates frequencies of each suit from hand
    // adds them to integer ArrayList
    int hearts = Collections.frequency(handSuits, "Hearts");
    int diamonds = Collections.frequency(handSuits, "Diamonds");
    int clubs = Collections.frequency(handSuits, "Clubs");
    int spades = Collections.frequency(handSuits, "Spades");
    ArrayList<Integer> suitFrequency = new ArrayList<Integer>();
    suitFrequency.add(hearts);
    suitFrequency.add(diamonds);
    suitFrequency.add(clubs);
    suitFrequency.add(spades);

    // ArrayList of all suits' full names
    ArrayList<String> suits = new ArrayList<String>();
    suits.add("Hearts");
    suits.add("Diamonds");
    suits.add("Clubs");
    suits.add("Spades");

    // remove turnedOver frequency and suit
    for (int l = 0; l < suitFrequency.size(); l++) {
      if (suits.get(l).equals(turnedOver)) {
        suitFrequency.remove(l);
        suits.remove(l);
      } // end if
    } // end for

    // bubble sort
    // puts suit with highest frequency last in suits and suitFrequency
    for (int j = 0; j < 2; j++) {
      for (int k = 0; k < 2; k++) {
        if (suitFrequency.get(k) > suitFrequency.get(k + 1)) {
          Collections.swap(suitFrequency, k, k + 1);
          Collections.swap(suits, k, k + 1);
        } // end swap if
      } // end k for
    } // end j for

    // if dealer, chooses suit with highest frequency as trump
    if (this.dealer == true) {
      decision = suits.get(2);

    } else {
      // passes if hand doesn't have at least three of same suit
      if (suitFrequency.get(2) > 2) {
        decision = suits.get(2);
      } else {
        decision = "passing";
      } // end set decision
    } // end if dealer
    return decision;
  } // end name()

  // handles AI decision during dealer discard phase
  // removes lowest card in deck
  // adds ordered up card to hand
  public void discard(Card turnedUp) {
    // removes last card because hand is already sorted
      // to have lowest card be last
    this.hand.removeCard(4);
    this.hand.addCard(turnedUp);
    this.hand.sortHand();
  } // end discard()

  // handles AI decisions during going alone phase
  // only goes in alone if AI has 5 of trump
  public boolean alone(String trump) {
    boolean decision = false;
    int numTrump = this.hand.getTrumpFrequency(trump);
    if (numTrump == 5) {
      decision = true;
    } // end if
    return decision;
  } // end alone()

  // handles AI decisions for each turn
  // always plays worst card in hand because ai are dumb
  public Card play(String currentSuit) {
    int last = this.hand.getHandSize() - 1;
    Card played = this.hand.getCard(last);

    // doesn't have to follow suit because no suit was led
    if (currentSuit.equals("none")) {
      played = this.hand.getCard(last);
      this.hand.removeCard(last);

    } else {
      boolean hasCurrentSuit = false;
      // checks to see if AI has a suit in hand that matches led suit
      for (int i = 0; i < this.hand.getHandSize(); i++) {
        if (this.hand.getCard(i).getSuit().equals(currentSuit)) {
          hasCurrentSuit = true;
        } // end if hand has currentSuit
      } // end for every card in hand
      boolean keepGoing = true;
      while (keepGoing) {
        // selects lowest card following suit
        if (hasCurrentSuit == true) {
          if (this.hand.getCard(last).getSuit().equals(currentSuit)) {
            played = this.hand.getCard(last);
            this.hand.removeCard(last);
            keepGoing = false;
          } else {
            last -= 1;
          } // end if last card follows suit

        // selects lowest card without having to follow suit   
        } else {
          played = this.hand.getCard(last);
          this.hand.removeCard(last);
          keepGoing = false;
        } // end if hasCurrentSuit
      } // end while
    } // end if currentSuit
    return played;
  } // end play()

  // tests AI class
  public static void main(String[] args) {
    Deck deck = new Deck();
    AI ai = new AI(deck, "ai");
    System.out.println("ai hand after sort:");
    ai.hand.getHand();
    System.out.println();

    // tests order()
    int randNum = (int) (Math.random() * (deck.getDeckSize()));
    Card turnedUp = deck.getCard(randNum);
    System.out.println(turnedUp.getSuit());
    System.out.println(turnedUp.getName());
    System.out.println(ai.order(turnedUp));
    System.out.println();

    System.out.println(ai.name("Hearts"));
    System.out.println(ai.name("Diamonds"));
    System.out.println(ai.name("Clubs"));
    System.out.println(ai.name("Spades"));
    System.out.println();

    ai.hand.getHand();
    ai.discard(turnedUp);
    System.out.println();
    ai.hand.getHand();

  } // end public static void main(String[] args)
} // end AI.java
