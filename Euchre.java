// Euchre.java

import java.util.*;
import java.io.*;

public class Euchre {
  protected Deck deck = new Deck();
  protected ArrayList<Card> inPlay = new ArrayList<Card>();
  protected ArrayList<TeamMember> players = new ArrayList<TeamMember>();
  protected Player player;
  protected AI teammate;
  protected AI ai;
  protected AI aitm;
  protected int pScore = 0;
  protected int oScore = 0;
  protected int pTricks = 0;
  protected int oTricks = 0;
  protected int winPoints;
  protected String trump;

  public static void main(String[] args) {

    //clears screen
    System.out.print("\033[H\033[2J");
    System.out.println("Welcome to Euchre Without Friends!");
    boolean getPoints = true;
    while (getPoints) {
      try {
        System.out.println();
        System.out.print("Would you like to play to 5 or 10 points? ");
        Scanner pointsInput = new Scanner(System.in);
        int pointsResponse = pointsInput.nextInt();

        if (pointsResponse == 5) {
          getPoints = false;
          new Euchre(5);
        } else if (pointsResponse == 10) {
          getPoints = false;
          new Euchre(10);
        } else {
          System.out.println("Please enter a valid response.");
        } // end if
      } catch (Exception e) {
        System.out.println("Please enter a valid response.");
      } // end catch
    } // end while
  } // end public static void main(String[] args)

  public Euchre(int winPoints) {

    ai = new AI(deck, "ai");
    players.add(ai);
    teammate = new AI(deck, "teammate");
    players.add(teammate);
    aitm = new AI(deck, "aitm");
    players.add(aitm);
    player = new Player(deck, "player");
    players.add(player);


    this.winPoints = winPoints;
    //boolean winner = false;
    int winner = 0;
    //while (winner < 6) {
      deck.setDeck();
      String lastDealer = getLastDealer();
      players.clear();
      /* prints deck
      for (int i = 0; i < 24; i++) {
        System.out.println(deck.getCard(i).getName());
      } // end for
      System.out.println();
      */

      ai = new AI(deck, "ai");
      players.add(ai);
      teammate = new AI(deck, "teammate");
      players.add(teammate);
      aitm = new AI(deck, "aitm");
      players.add(aitm);
      player = new Player(deck, "player");
      players.add(player);

      setDealer(lastDealer);

      /* used to test setDealer()
      System.out.println(players.get(3).getName());
      winner += 1;
      */

      orderTrump();
    //} // end game
  } // end constructor


  public String getLastDealer() {
    String lastDealer;
    // if last player in players isn't dealer, then game just started
      // because dealer is always last and there is no dealer
    // set aitm to dealer so setDealer rotates into player being dealer
    if (players.get(3).getDealer() == false) {
      lastDealer = "ai";
    // otherwise set lastDealer to whoever was last in array
    } else {
      lastDealer = players.get(3).getName();
    } // end if
    return lastDealer;
  } // end getLastDealer()

  // sets up order of players ArrayList
  public void setDealer(String lastDealer) {
    int dealerPos = 0;

    // get last dealer's position and set it to dealerPos
    for (int i = 0; i < 4; i++) {
      if (players.get(i).getName().equals(lastDealer)) {
        dealerPos = i;
      } // end if
    } // end for

    // if last dealer's position is greater,
      // decrement by one to get new dealer's position
    // otherwise set new dealer's position to 3
    if (dealerPos > 0) {
      dealerPos -= 1;
      // set new dealer to true
      players.get(dealerPos).setDealer(true);
      // set last dealer to false
      players.get(dealerPos + 1).setDealer(false);
    } else {
      dealerPos = 3;
      players.get(3).setDealer(true);
      players.get(0).setDealer(false);
    } // end if

    if (dealerPos == 2) {
      Collections.rotate(players, 1);
    } else if (dealerPos == 1) {
      //rotates players twice
      Collections.rotate(players, 2);
    } else if (dealerPos == 0) {
      //rotates players three times
      Collections.rotate(players, 3);
    } // end if
  } // end setDealer()

  // used to finalize hands and how to decide trump
  public void orderTrump() {
    int randNum = (int) (Math.random() * (deck.getDeckSize()));
    Card turnedUp = deck.getCard(randNum);
    int numPassing = 0;
    String turnedOver;

    while (numPassing < 4) {
      TeamMember currentPlayer = players.get(numPassing);

      if (currentPlayer.getName().equals("player")) {
        // clears screen
        System.out.print("\033[H\033[2J");
        System.out.println("Ordering Phase");
        System.out.println();
        getScores();
        System.out.println("Turned up card: " + turnedUp.getName());
        System.out.println("Your hand:");
        currentPlayer.hand.getHand();
        System.out.println();
        // set decision to returned string from order() from Player.java
        String decision = currentPlayer.order(turnedUp);

        if (decision.equals("p")) {
          if (currentPlayer.getDealer() == true) {
            turnedOver = turnedUp.getSuit();
            // dealer has index of 3 so inc numPassing by one to end keepGoing
            numPassing += 1;
            nameTrump(turnedOver);
          } else {
            numPassing += 1;
          } // end if

        } else if (decision.equals("o")) {
          turnedUp.setTrump(true);
          // set numPassing to 4 to end while
          numPassing = 4;

          for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
              Card currentCard = currentPlayer.hand.getCard(j);
              if (currentCard.getSuit().equals(turnedUp.getSuit())) {
                currentCard.setTrump(true);
                // if currentCard is a jack of trump suit, make it best card
                if (currentCard.getRank() == 7) {
                  currentCard.setRank(1);
                } // end if card is jack
              } // end if card is same suit as turnedUp
            } // end for every card in player's hand

            String oppositeSuit;
            if (turnedUp.getSuit().equals("Hearts")) {
              oppositeSuit = "Diamonds";
            } else if (turnedUp.getSuit().equals("Diamonds")) {
              oppositeSuit = "Hearts";
            } else if (turnedUp.getSuit().equals("Clubs")) {
              oppositeSuit = "Spades";
            } else {
              oppositeSuit = "Clubs";
            } // end if

            for (int k = 0; k < 5; k++) {
              Card currentCard = currentPlayer.hand.getCard(k);
              if (currentCard.getSuit().equals(oppositeSuit)) {
                // if currentCard is a jack of opposite trump suit,
                  // make it second best card
                if (currentCard.getRank() == 7) {
                  currentCard.setTrump(true);
                  currentCard.setRank(2);
                } // end if card is jack
              } // end if card is opposite suit
            } // end for every card in player's hand
          } // end for every player
          this.trump = turnedUp.getSuit();
          dealerDiscard(turnedUp);
        } // end if pass or order

      } else {
        //numPassing += 1;

        String decision = currentPlayer.order(turnedUp);

        if (decision.equals("passing")) {
          if (currentPlayer.getDealer() == true) {
            turnedOver = turnedUp.getSuit();
            numPassing += 1;
            nameTrump(turnedOver);
          } else {
            numPassing += 1;
          } // end if

        } else {
          turnedUp.setTrump(true);
          numPassing = 4;
          for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
              Card currentCard = currentPlayer.hand.getCard(j);
              if (currentCard.getSuit().equals(turnedUp.getSuit())) {
                currentCard.setTrump(true);
                // if currentCard is a jack of trump suit, make it best card
                if (currentCard.getRank() == 7) {
                  currentCard.setRank(1);
                } // end if card is jack
              } // end if card is same suit as turnedUp
            } // end for every card in player's hand

            String oppositeSuit;
            if (turnedUp.getSuit().equals("Hearts")) {
              oppositeSuit = "Diamonds";
            } else if (turnedUp.getSuit().equals("Diamonds")) {
              oppositeSuit = "Hearts";
            } else if (turnedUp.getSuit().equals("Clubs")) {
              oppositeSuit = "Spades";
            } else {
              oppositeSuit = "Clubs";
            } // end if

            for (int k = 0; k < 5; k++) {
              Card currentCard = currentPlayer.hand.getCard(k);
              if (currentCard.getSuit().equals(oppositeSuit)) {
                // if currentCard is a jack of opposite trump suit,
                  // make it second best card
                if (currentCard.getRank() == 7) {
                  currentCard.setTrump(true);
                  currentCard.setRank(2);
                } // end if card is jack
              } // end if card is opposite suit
            } // end for every card in player's hand
          } // end for every player
          this.trump = turnedUp.getSuit();
          dealerDiscard(turnedUp);
        } // end if pass or order
      } // end if player or ai
    } // end while
  } // end orderTrump()

  public void nameTrump(String turnedOver) {
    int numPassing = 0;

    while (numPassing < 4) {
      TeamMember currentPlayer = players.get(numPassing);

      if (currentPlayer.getName().equals("player")) {
        System.out.print("\033[H\033[2J");
        System.out.println("Naming Phase");
        System.out.println();
        getScores();
        System.out.println("Turned over suit: " + turnedOver);
        System.out.println("Your hand:");
        currentPlayer.hand.getHand();
        System.out.println();
        String decision = currentPlayer.name(turnedOver);

        if (currentPlayer.getDealer() == true) {
          numPassing = 4;
          for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
              Card currentCard = currentPlayer.hand.getCard(j);
              if (currentCard.getSuit().equals(decision)) {
                currentCard.setTrump(true);
                // if currentCard is a jack of trump suit, make it best card
                if (currentCard.getRank() == 7) {
                  currentCard.setRank(1);
                } // end if card is jack
              } // end if card is same suit as turnedUp
            } // end for every card in player's hand

            String oppositeSuit;
            if (decision.equals("Hearts")) {
              oppositeSuit = "Diamonds";
            } else if (decision.equals("Diamonds")) {
              oppositeSuit = "Hearts";
            } else if (decision.equals("Clubs")) {
              oppositeSuit = "Spades";
            } else {
              oppositeSuit = "Clubs";
            } // end if

            for (int k = 0; k < 5; k++) {
              Card currentCard = currentPlayer.hand.getCard(k);
              if (currentCard.getSuit().equals(oppositeSuit)) {
                // if currentCard is a jack of opposite trump suit,
                  // make it second best card
                if (currentCard.getRank() == 7) {
                  currentCard.setTrump(true);
                  currentCard.setRank(2);
                } // end if card is jack
              } // end if card is opposite suit
            } // end for every card in player's hand
          } // end for every player
          this.trump = decision;

        } else {
          if (decision.equals("p")) {
            numPassing += 1;
          } else {
            numPassing = 4;
            for (int i = 0; i < 4; i++) {
              for (int j = 0; j < 5; j++) {
                Card currentCard = currentPlayer.hand.getCard(j);
                if (currentCard.getSuit().equals(decision)) {
                  currentCard.setTrump(true);
                  // if currentCard is a jack of trump suit, make it best card
                  if (currentCard.getRank() == 7) {
                    currentCard.setRank(1);
                  } // end if card is jack
                } // end if card is same suit as turnedUp
              } // end for every card in player's hand

              String oppositeSuit;
              if (decision.equals("Hearts")) {
                oppositeSuit = "Diamonds";
              } else if (decision.equals("Diamonds")) {
                oppositeSuit = "Hearts";
              } else if (decision.equals("Clubs")) {
                oppositeSuit = "Spades";
              } else {
                oppositeSuit = "Clubs";
              } // end if

              for (int k = 0; k < 5; k++) {
                Card currentCard = currentPlayer.hand.getCard(k);
                if (currentCard.getSuit().equals(oppositeSuit)) {
                  // if currentCard is a jack of opposite trump suit,
                    // make it second best card
                  if (currentCard.getRank() == 7) {
                    currentCard.setTrump(true);
                    currentCard.setRank(2);
                  } // end if card is jack
                } // end if card is opposite suit
              } // end for every card in player's hand
            } // end for every player
            this.trump = decision;
          } // end if pass or suit
        } // end if dealer


      } else {
        String decision = currentPlayer.name(turnedOver);

        if (currentPlayer.getDealer() == true) {
          numPassing = 4;
          for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
              Card currentCard = currentPlayer.hand.getCard(j);
              if (currentCard.getSuit().equals(decision)) {
                currentCard.setTrump(true);
                // if currentCard is a jack of trump suit, make it best card
                if (currentCard.getRank() == 7) {
                  currentCard.setRank(1);
                } // end if card is jack
              } // end if card is same suit as turnedUp
            } // end for every card in player's hand

            String oppositeSuit;
            if (decision.equals("Hearts")) {
              oppositeSuit = "Diamonds";
            } else if (decision.equals("Diamonds")) {
              oppositeSuit = "Hearts";
            } else if (decision.equals("Clubs")) {
              oppositeSuit = "Spades";
            } else {
              oppositeSuit = "Clubs";
            } // end if

            for (int k = 0; k < 5; k++) {
              Card currentCard = currentPlayer.hand.getCard(k);
              if (currentCard.getSuit().equals(oppositeSuit)) {
                // if currentCard is a jack of opposite trump suit,
                  // make it second best card
                if (currentCard.getRank() == 7) {
                  currentCard.setTrump(true);
                  currentCard.setRank(2);
                } // end if card is jack
              } // end if card is opposite suit
            } // end for every card in player's hand
          } // end for every player
          this.trump = decision;

        } else {
          if (decision.equals("passing")) {
            numPassing += 1;
          } else {
            numPassing = 4;
            for (int i = 0; i < 4; i++) {
              for (int j = 0; j < 5; j++) {
                Card currentCard = currentPlayer.hand.getCard(j);
                if (currentCard.getSuit().equals(decision)) {
                  currentCard.setTrump(true);
                  // if currentCard is a jack of trump suit, make it best card
                  if (currentCard.getRank() == 7) {
                    currentCard.setRank(1);
                  } // end if card is jack
                } // end if card is same suit as turnedUp
              } // end for every card in player's hand

              String oppositeSuit;
              if (decision.equals("Hearts")) {
                oppositeSuit = "Diamonds";
              } else if (decision.equals("Diamonds")) {
                oppositeSuit = "Hearts";
              } else if (decision.equals("Clubs")) {
                oppositeSuit = "Spades";
              } else {
                oppositeSuit = "Clubs";
              } // end if

              for (int k = 0; k < 5; k++) {
                Card currentCard = currentPlayer.hand.getCard(k);
                if (currentCard.getSuit().equals(oppositeSuit)) {
                  // if currentCard is a jack of opposite trump suit,
                    // make it second best card
                  if (currentCard.getRank() == 7) {
                    currentCard.setTrump(true);
                    currentCard.setRank(2);
                  } // end if card is jack
                } // end if card is opposite suit
              } // end for every card in player's hand
            } // end for every player
            this.trump = decision;
          } // end if pass or suit
        } // end if dealer
      } // end if player or AI
    } // end while numPassing
  } // end nameTrump()

  public void dealerDiscard(Card turnedUp) {
    TeamMember currentDealer = players.get(3);

    if (currentDealer.getName().equals("player")) {
      // clears screen
      System.out.print("\033[H\033[2J");
      System.out.println("Dealer Discarding Phase");
      System.out.println();
      getScores();
      System.out.println("Trump: " + this.trump);
      System.out.println("Turned up card: " + turnedUp.getName());
      System.out.println("Your hand:");
      currentDealer.getPlayerHand();
      System.out.println();
      currentDealer.discard(turnedUp);
    } else {
      currentDealer.discard(turnedUp);
    } // end if player or AI
  } // end dealerDiscard()

  // increases player's team score based on how many points were won that round
  public void incPScore(int i) {
    this.pScore += i;
  } // end incPScore()

  // increases opponent's team score based on how many points were won that round
  public void incOScore(int i) {
    this.oScore += i;
  } // end incOScore()

  public void getScores() {
    System.out.println("Your score: " + this.pScore);
    System.out.println("Opponent's score: " + this.oScore);
  } // end getScores()

  // increases player's team tricks by one
  public void incPTricks() {
    this.pTricks += 1;
  } // end incPTricks()

  // increases opponent's team tricks by one
  public void incOTricks() {
    this.oTricks +=1;
  } // end incOTricks()

  public void getTricks() {
    System.out.println("Your tricks: " + this.pTricks);
    System.out.println("Opponenet's tricks: " + this.oTricks);
  } // end getTricks()
} // end Euchre.java

//public class invalidResponse extends Exception {}
