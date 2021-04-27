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

    //new Euchre(10);
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
    boolean winner = false;
    while (!winner) {
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
      goingAlone();
      turn();
      getWinner();


      // check to see if there is a winner
      if (this.pScore >= winPoints) {
        System.out.println("Congrats! You won!");
        winner = true;
      } else if (this.oScore >= winPoints) {
        System.out.println("You lost :(");
        winner = true;
      } // end check winner

    } // end game
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
          this.trump = turnedUp.getSuit();
          currentPlayer.setTrumpDecision(true);
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
          //this.trump = turnedUp.getSuit();
          dealerDiscard(turnedUp);
        } // end if pass or order

      } else {
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
          this.trump = turnedUp.getSuit();
          this.ai.setTrumpDecision(true);
          this.aitm.setTrumpDecision(true);
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
        System.out.println(decision);

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
              //this.trump = "Hearts";
            } else if (decision.equals("Diamonds")) {
              oppositeSuit = "Hearts";
              //this.trump = "Diamonds";
            } else if (decision.equals("Clubs")) {
              oppositeSuit = "Spades";
              //this.trump = "Clubs";
            } else {
              oppositeSuit = "Clubs";
              //this.trump = "Spades";
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
            player.setTrumpDecision(true);
            this.trump = decision;
            currentPlayer.setTrumpDecision(true);

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
            if (!(decision.equals("p"))){
              this.trump = decision;
              currentPlayer.setTrumpDecision(true);
            } // end if
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
          this.ai.setTrumpDecision(true);
          this.aitm.setTrumpDecision(true);

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
            this.ai.setTrumpDecision(true);
            this.aitm.setTrumpDecision(true);
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

  // doesn't allow teammate to go alone
    // because then player can't player and that's boring
  public void goingAlone() {
    for (int i = 0; i < 4; i++) {
      TeamMember currentPlayer = players.get(i);

      if (currentPlayer.getName().equals("player")) {
        System.out.print("\033[H\033[2J");
        System.out.println("Going Alone Phase");
        System.out.println();
        getScores();
        System.out.println("Trump: " + this.trump);
        System.out.println("Your hand:");
        currentPlayer.hand.getHand();
        System.out.println();
        boolean decision = currentPlayer.alone(this.trump);

        if (decision == true) {
          for (int j = 0; j < players.size(); j++) {
            TeamMember findTeammate = players.get(j);
            // remove teammate from ArrayList so they can't play
            if (findTeammate.getName().equals("teammate")) {
              players.remove(j);
            } // end if
          } // end search for teammate
          this.player.setAlone(true);
        } // end if player is going alone

      } else if (currentPlayer.getName().equals("ai")) {
        boolean decision = currentPlayer.alone(this.trump);
        if (decision == true) {
          for (int j = 0; j < players.size(); j++) {
            TeamMember findTeammate = players.get(j);
            // remove aitm from ArrayList so they can't play
            if (findTeammate.getName().equals("aitm")) {
              players.remove(j);
            } // end if
          } // end search for teammate
          this.ai.setAlone(true);
          this.aitm.setAlone(true);
        } // end if player is going alone

      } else if (currentPlayer.getName().equals("aitm")) {
        boolean decision = currentPlayer.alone(this.trump);
        if (decision == true) {
          for (int j = 0; j < players.size(); j++) {
            TeamMember findTeammate = players.get(j);
            // remove ai from ArrayList so they can't play
            if (findTeammate.getName().equals("ai")) {
              players.remove(j);
            } // end if
          } // end search for teammate
          this.ai.setAlone(true);
          this.aitm.setAlone(true);
        } // end if player is going alone
      }// end if player or AI
    } // end for every player
  } // end goingAlone()

  public void turn() {
    int turns = 1;
    while (turns < 6) {
      String currentSuit = "none";
      for (int i = 0; i < players.size(); i++) {
        TeamMember currentPlayer = players.get(i);
        if (currentPlayer.getName().equals("player")) {
          System.out.print("\033[H\033[2J");
          System.out.println("Your Turn");
          System.out.println();
          getScores();
          getTricks();
          System.out.println("Turn #" + turns);
          System.out.println("Trump: " + this.trump);
          System.out.println();

          System.out.println("Cards in play:");
          if (inPlay.size() == 0) {
            System.out.println("  none");
          } else {
            for (int j = 0; j < inPlay.size(); j++) {
              System.out.println("  " + players.get(j).getName() + ": " + inPlay.get(j).getName());
            } // end for
          } // end if cards in play
          System.out.println();

          System.out.println("Your hand:");
          currentPlayer.getPlayerHand();
          System.out.println();
          Card played = currentPlayer.play(currentSuit);
          inPlay.add(played);

          // sets what suit players need to follow
          if (currentSuit.equals("none")) {
            currentSuit = played.getSuit();
          } // end if

        } else {
          Card played = currentPlayer.play(currentSuit);
          inPlay.add(played);

          // sets what suit players need to follow
          if (currentSuit.equals("none")) {
            currentSuit = played.getSuit();
          } // end if
        } // end if player or AI
      } // end for every player

      // look for card with lowest number as rank that follow suit
      // this card wins the trick
      int bestCard = 0;
      String winner = "player";
      for (int k = 0; k < (inPlay.size() - 1); k++) {
        Card currentCard = inPlay.get(k);
        if (currentCard.getRank() < inPlay.get(k + 1).getRank()) {
          if (currentCard.getSuit().equals(currentSuit)) {
            bestCard = k;
          } // end follow suit
        } else {
          bestCard = k + 1;
        } // end if currentCard is best card
      } // end check for best card

      // increase tricks for winner of current turn
      winner = players.get(bestCard).getName();
      if (winner.equals("player")) {
        incPTricks();
      } else if (winner.equals("teammate")) {
        incPTricks();
      } else {
        incOTricks();
      } // end increase tricks

      inPlay.clear();
      turns += 1;
    } // end while
  } // end turn()

  public void getWinner() {
    TeamMember winner = this.player;
    int numTricks = this.pTricks;

    if (this.oTricks > 2) {
      winner = this.ai;
      numTricks = this.oTricks;
    } // end switch winner

    // scoring system
    // yes it is way too complicated
    if (winner.getTrumpDecision() == true) {
      if (winner.getAlone() == true) {
        if (numTricks == 5) {
          if (winner == this.player) {
            incPScore(4);
          } else {
            incOScore(4);
          } // end if player or AI
        } else {
          if (winner == this.player) {
            incPScore(2);
          } else {
            incOScore(2);
          } // end if player or AI
        } // end if numTricks while alone
      } else {
        if (numTricks == 5) {
          if (winner == this.player) {
            incPScore(2);
          } else {
            incOScore(2);
          } // end if player or AI
        } else {
          if (winner == this.player) {
            incPScore(1);
          } else {
            incOScore(1);
          } // end if player or AI
        } // end if numTricks while not alone
      } // end if winner chosee to go alone
    } else {
      if (winner.getAlone() == true) {
        if (numTricks == 5) {
          if (winner == this.player) {
            incPScore(5);
          } else {
            incOScore(5);
          } // end if player or AI
        } else {
          if (winner == this.player) {
            incPScore(4);
          } else {
            incOScore(4);
          } // end if player or AI
        } // end if numTricks while alone
      } else {
        if (numTricks == 5) {
          if (winner == this.player) {
            incPScore(4);
          } else {
            incOScore(4);
          } // end if player or AI
        } else {
          if (winner == this.player) {
            incPScore(2);
          } else {
            incOScore(2);
          } // end if player or AI
        } // end if numTricks while not alone
      } // end if winner chosee to go alone
    } // end if winner chose trump

    this.pTricks = 0;
    this.oTricks = 0;
  } // end getWinner()

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
    System.out.println("Opponent's tricks: " + this.oTricks);
  } // end getTricks()
} // end Euchre.java

//public class invalidResponse extends Exception {}
