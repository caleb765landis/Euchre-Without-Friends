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

  public String name(String turnedOver) {
    String decision;
    ArrayList<String> handSuits = new ArrayList<String>();
    for (int i = 0; i < 5; i++) {
      handSuits.add(this.hand.getCard(i).getSuit());
    } // end for

    int hearts = Collections.frequency(handSuits, "Hearts");
    int diamonds = Collections.frequency(handSuits, "Diamonds");
    int clubs = Collections.frequency(handSuits, "Clubs");
    int spades = Collections.frequency(handSuits, "Spades");
    ArrayList<Integer> suitFrequency = new ArrayList<Integer>();
    suitFrequency.add(hearts);
    suitFrequency.add(diamonds);
    suitFrequency.add(clubs);
    suitFrequency.add(spades);

    ArrayList<String> suits = new ArrayList<String>();
    suits.add("Hearts");
    suits.add("Diamonds");
    suits.add("Clubs");
    suits.add("Spades");

    // remove turnedOver frequency
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

    if (this.dealer == true) {
      decision = suits.get(2);
    } else {
      if (suitFrequency.get(2) > 2) {
        decision = suits.get(2);
      } else {
        decision = "passing";
      } // end set decision
    } // end if dealer
    return decision;
  } // end name()

  public void discard(Card turnedUp) {
    this.hand.removeCard(4);
    this.hand.addCard(turnedUp);
    this.hand.sortHand();
  } // end discard()

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
