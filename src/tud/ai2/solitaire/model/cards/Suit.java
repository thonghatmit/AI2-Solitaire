package tud.ai2.solitaire.model.cards;

/**
 * Enumeration tud.ai2.solitaire.model.cards.Suit
 * contains a ordered listing of items which represent a suit.
 * A carddeck contains 13 cards of every suit so there is a total amount of 52 card in use.
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow)
 * @version 1.0
 */
public enum Suit {
    CLUBS("Clubs"), DIAMONDS("Diamonds"), HEARTS("Hearts"), SPADES("Spades");

    private final String representation;

    Suit(String r) {
        representation = r;
    }

    public String string() {
        return representation;
    }
}
