// Player.java

import java.util.*;

public class Player extends TeamMember {

  public Player(){}

  public Player(Deck deck, String name) {
    this.hand = new Hand();
    this.hand.dealHand(deck);
    this.hand.sortHand();
    this.dealer = false;
    this.alone = false;
    this.name = name;
  } // end constructor

  // ???
  public void getPlayerHand() {
    this.hand.getHand();
  }

  public String order(Card turnedUp) {
    boolean keepGoing = true;
    String decision = "p";
    while (keepGoing) {
      System.out.println("Will you pass or order up?");
      System.out.print("p or o: ");
      Scanner input = new Scanner(System.in);
      String response = input.nextLine();
      //String decision;
      if (response.equals("p")) {
        keepGoing = false;
        decision = "p";
      } else if (response.equals("o")) {
        keepGoing = false;
        decision = "o";
      } else {
        System.out.println("Please enter a valid response.");
      } // end if
    } // end while
    return decision;
  } // end order()

  // tests Player class
  public static void main(String[] args) {
    Deck deck = new Deck();
    Player p1 = new Player(deck, "p1");
    Player p2 = new Player(deck, "p2");

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
  } // end public static void main(String[] args)
} // end Player.java
