Euchre.class: Euchre.java Card.class Deck.class Hand.class TeamMember.class Player.class AI.class
	javac -g Euchre.java

Card.class: Card.java
	javac -g Card.java

Deck.class: Deck.java Card.class
	javac -g Deck.java

Hand.class: Hand.java Deck.class Card.class
	javac -g Hand.java

TeamMember.class: TeamMember.java Hand.class Deck.class Card.class
	javac -g TeamMember.java

Player.class: Player.java TeamMember.class Hand.class Deck.class Card.class
	javac -g Player.java

AI.class: AI.java TeamMember.class Hand.class Deck.class Card.class
	javac -g AI.java

clean:
	rm *.class

run: Euchre.class
	java Euchre

debug: Euchre.class
	jdb Euchre
