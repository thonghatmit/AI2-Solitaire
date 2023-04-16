package tud.ai2.solitaire.model;

import tud.ai2.solitaire.model.cards.AbstractCard;
import tud.ai2.solitaire.model.cards.CardValue;
import tud.ai2.solitaire.model.cards.Suit;

/**
 * Class tud.ai2.solitaire.model.Rules
 * contains methods which can be used to check for legal operations.
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow)
 * @version 1.0
 */
public class Rules {

    /** legalListMove method
     * this method is used to check if a concatenation of two card lists is a legal move.
     * It is necessary if you want to combine two separate card piles.
     * By looking up the order of the cards which are going to connect those piles,
     * assuming the lists are well ordered(no doublecheck)
     *
     * @param top is the most upper card of the underlying card pile
     * @param bottom is the lowest card of the upper card pile
     * @return return true if those conditions are met and the piles are compatible
     */
    public static boolean legalListMove(AbstractCard top, AbstractCard bottom) {
        if(bottom == null) {
            return top.getValue() == CardValue.KING;
        }
        if(!bottom.isRevealed()) {
            return false;
        }
        //oneLower is true if relation of topcard to bottom card is correct by comparing the enum position
        boolean oneLower = top.getValue().ordinal() +1 == bottom.getValue().ordinal();
        //differentColor is true if the two suits have a different color, e.g. red and black or vice versa
        boolean differentColor =
                ((top.getSuit() == Suit.CLUBS || top.getSuit() == Suit.SPADES)
                        && (bottom.getSuit() == Suit.HEARTS || bottom.getSuit() == Suit.DIAMONDS))
                        ||
                        ((top.getSuit() == Suit.HEARTS || top.getSuit() == Suit.DIAMONDS)
                                && (bottom.getSuit() == Suit.CLUBS || bottom.getSuit() == Suit.SPADES));
        return oneLower && differentColor;
    }

    /** legalStackMove method
     * this method is used to check if a concatenation of two card lists is a legal move
     * considering that bottom parameter is part of one of four upper stacks.
     * Those stacks have an other order of cards,
     * therefore cards with the same suit are accepted and keeping the solitaire order from ace to king
     * assuming the lists are well ordered(no doublecheck)
     *
     * @param top is the most upper card of the underlying card stack
     * @param bottom is the lowest card of the upper card stack
     * @return return true if those conditions are met and the stacks are compatible
     */
    public static boolean  legalStackMove(AbstractCard top, AbstractCard bottom){
        if(bottom == null && top.getValue() == CardValue.ACE) {
            return true;
        }
        else if(bottom == null) {
            return false;
        }
        //check of order
        boolean oneBigger = top.getValue().ordinal() == bottom.getValue().ordinal() + 1;
        //check of same suit
        boolean sameSuit = top.getSuit() == bottom.getSuit();
        return oneBigger && sameSuit;
    }

}
