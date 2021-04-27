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
    this.choseTrump = false;
  } // end constructor

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
        System.out.println();
      } // end if
    } // end while
    return decision;
  } // end order()

  public String name(String turnedOver) {
    boolean keepGoing = true;
    String decision = "p";
    ArrayList<String> suits = new ArrayList<String>();
    suits.add("Hearts");
    suits.add("Diamonds");
    suits.add("Clubs");
    suits.add("Spades");



    ArrayList<Character> suitOptions = new ArrayList<Character>();
    for (int i = 0; i < suits.size(); i++) {
      if (suits.get(i) != turnedOver) {
        suitOptions.add(Character.toLowerCase(suits.get(i).charAt(0)));
      }// end if
    } // end for

    for (int m = 0; m < suits.size(); m++) {
      if (suits.get(m).equals(turnedOver)) {
        suits.remove(m);
      }
    }

    while (keepGoing) {
      if (this.dealer == true) {
        System.out.println("Which suit would you like to be trump?");
        System.out.print(suitOptions.get(0) + ", ");
        System.out.print(suitOptions.get(1) + ", ");
        System.out.print(suitOptions.get(2) + ": ");
        Scanner input = new Scanner(System.in);
        Character response = input.next().charAt(0);

        int checkSuit = 0;
        for (int k = 0; k < 3; k++) {
          if (response == suitOptions.get(k)) {
                decision = suits.get(k);
          } else {
            checkSuit += 1;
          } // end if response is in suitOptions
        } // end for each suit in suitOptions
        if (checkSuit == 3) {
          System.out.println("Please enter a valid response.");
          System.out.println();
        } else {
          keepGoing = false;
        } // end if

      } else {
        System.out.println("Would you like to pass or name trump?");
        System.out.println("p, "  + suitOptions.get(0) + ", ");
        System.out.println(suitOptions.get(1) + ", ");
        Scanner input = new Scanner(System.in);
        Character response = input.next().charAt(0);

        if (response == 'p') {
          decision = "p";
          keepGoing = false;
        } else {
          int checkSuit = 0;
          for (int k = 0; k < 3; k++) {
            if (response.equals(suitOptions.get(k))) {
                  decision = suits.get(k);
            } else {
              checkSuit += 1;
            } // end check if response matches available suit
          } // end for
          if (checkSuit == 3) {
            System.out.println("Please enter a valid response.");
            System.out.println();
          } else {
            keepGoing = false;
          } // end if not a suit
        } // end if pass or suit
      } // end if dealer
    } // end while
    return decision;
  } // end name()

  public void discard(Card turnedUp) {
    boolean keepGoing = true;
    while (keepGoing) {
      try {
        System.out.print("Which card would you like to discard? ");
        Scanner input = new Scanner(System.in);
        int response = input.nextInt();

        if (response < this.hand.getHandSize()) {
          this.hand.removeCard(response);
          this.hand.addCard(turnedUp);
          this.hand.sortHand();
          keepGoing = false;
        } else {
          System.out.println("Please enter a valid Response");
          System.out.println();
        }
      } catch (Exception e) {
        System.out.println("Please enter a valid response.");
        System.out.println();
      } // end try
    } // end while
  } // end discard()

  public boolean alone(String trump) {
    boolean decision = false;
    boolean keepGoing = true;
    while (keepGoing) {
      System.out.println("Would you like to go in alone?");
      System.out.print("y or n: ");
      Scanner input = new Scanner(System.in);
      String response = input.nextLine();

      if (response.equals("y")) {
        decision = true;
        keepGoing = false;
      } else if (response.equals("n")) {
        keepGoing = false;
      } else {
        System.out.println("Please enter a valid response.");
        System.out.println();
      } // end if
    } // end while
    return decision;
  } // end alone()

  public Card play(String currentSuit) {
    Card played = this.hand.getCard(0);
    boolean keepGoing = true;
    while (keepGoing) {
      try {
        if (!(currentSuit.equals("none"))) {
          System.out.println(currentSuit + " was led.");
        } // end if
        System.out.print("Which card would you like to play? ");
        Scanner input = new Scanner(System.in);
        int response = input.nextInt();

        if (response < this.hand.getHandSize()) {
          if (currentSuit.equals("none")) {
            played = this.hand.getCard(response);
            this.hand.removeCard(response);
            keepGoing = false;
          } else {
            boolean hasCurrentSuit = false;
            for (int i = 0; i < this.hand.getHandSize(); i++) {
              if (this.hand.getCard(i).getSuit().equals(currentSuit)) {
                hasCurrentSuit = true;
              } // end if hand has currentSuit
            } // end for every card in hand

            // makes sure valid response follows suit if possible
            if (hasCurrentSuit == true) {
              if (this.hand.getCard(response).getSuit().equals(currentSuit)) {
                played = this.hand.getCard(response);
                this.hand.removeCard(response);
                keepGoing = false;
              } else {
                System.out.println("Selected card must follow suit.");
              } // end if card follows suit
            } else {
              played = this.hand.getCard(response);
              this.hand.removeCard(response);
              keepGoing = false;
            } // end if valid response
          } // end if currentSuit
        } else {
          System.out.println("Please enter a valid response.");
          System.out.println();
        }
      } catch (Exception e) {
        System.out.println("Please enter a valid response.");
        System.out.println();
      } // end try
    } // end while
    return played;
  } // end play()

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

    p1.name("Hearts");
  } // end public static void main(String[] args)
} // end Player.java
