package tud.ai2.solitaire.model.cards;

/**
 * Enumeration tud.ai2.solitaire.model.cards.CardValue
 * contains a ordered listing of items which represent a cardvalue.
 * order is from low to high value, respectively to solitaire
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow)
 * @version 1.0
 */
public enum CardValue {
    ACE("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K");

    /**
     * Attribute
     * Every cardvalue has its representation as a string attribute
     **/
    private final String representation;

    CardValue(String r) {
        representation = r;
    }

    public String string() {
        return representation;
    }

    public CardValue int2Value(int i) {
        return values()[i];
    }
}
