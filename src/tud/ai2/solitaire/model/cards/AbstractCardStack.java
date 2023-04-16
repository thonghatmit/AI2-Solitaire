package tud.ai2.solitaire.model.cards;

import tud.ai2.solitaire.view.PlayingField;

import java.awt.*;

/**tud.ai2.solitaire.model.cards.AbstractCardStack class
 * contains the implementation of the card stacks which are available on the upper side of the game
 * these stacks start empty and are filled with cards in the correct order by the player during the game
 * filling theses stacks is the main objective of the game
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow)
 * @version 1.0
 */
public abstract class AbstractCardStack {

    // attributes for the visual display
    private static int count = 0;
    private final int index;
    private Point position;

    /**tud.ai2.solitaire.model.cards.AbstractCardStack constructor
     * creates one of the four tud.ai2.solitaire.model.cards.CardStack elements
     */
    public AbstractCardStack(){
        this.index = count++;
        if(PlayingField.getPOSITIONS() != null)
            this.position = PlayingField.getPOSITIONS()[this.index];
    }

    /**push method
     * pushes a tud.ai2.solitaire.model.cards.Card on the stack. Does nothing if the stack is already full.
     *
     * @param cc the tud.ai2.solitaire.model.cards.Card that will be pushed onto the stack
     * @throws Exception 
     */
    public abstract void push(AbstractCard cc) throws Exception;

    /**pop method
     * returns the top tud.ai2.solitaire.model.cards.Card and removes it from the stack. Returns null if the stack is empty.
     *
     * @return the top tud.ai2.solitaire.model.cards.Card
     */
    public abstract AbstractCard pop();

    /**peek method
     * returns the top tud.ai2.solitaire.model.cards.Card without removing it.
     *
     * @return the top tud.ai2.solitaire.model.cards.Card
     */
    public abstract AbstractCard peek();

    /**isEmpty method
     *
     * @return true if the stack is empty, false otherwise
     */
    public abstract boolean isEmpty();

    /**isFull method
     *
     * @return true if the stack is full, false otherwise
     */
    public abstract boolean isFull();

    /**getSize method
     *
     * @return returns the number of cards in the stack
     */
    public abstract int size();

    /**getPosition method
     *
     * @return the position of this stack
     */
    public final Point getPosition(){
        return this.position;
    }

    /**getIndex method
     *
     * @return the index of this stack
     */
    public final int getIndex(){
        return this.index;
    }

    /**resetCount method
     * resets the counter of the stacks
     */
    public static void resetCount() {count = 0;}
}
