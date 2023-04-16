package tud.ai2.solitaire.model.cards;

import tud.ai2.solitaire.model.Rules;
import tud.ai2.solitaire.view.EndFrame;
import tud.ai2.solitaire.view.PlayingField;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * tud.ai2.solitaire.model.cards.CardContainer class
 * this class defines a card container, used to store cards and manage connections and card movement rules.
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow, Kurt Cieslinski)
 * @version 1.0
 */

public class CardContainer {

    //attributes define a card container
    private CardContainer previous = null;
    private AbstractCard card = null;
    private CardContainer next = null;

    private int listNumber;
    private Point initPos;

    private int stackNumber = -1;

    private static final int CARD_OFFSET = 22;

    private final LinkedList<Consumer<AbstractCard>> listeners = new LinkedList<>();

    /**
     * tud.ai2.solitaire.model.cards.CardContainer constructor
     * to create an empty card container object with a certain number on the playing field and initial position
     *
     * @param nr
     * @param initPos
     */
    public CardContainer(int nr, Point initPos) {
        this.listNumber = nr;
        this.initPos = initPos;
    }

    /**
     * tud.ai2.solitaire.model.cards.CardContainer constructor
     * creates a card container by given card parameter
     * it also provides the card with the mouse listeners for correct movement
     *
     * @param c
     */
    public CardContainer(AbstractCard c) {
        this.card = c;
        CardContainer containerReference = this;

        card.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                // disconnect from previous cards
                if (hasPrevious()) {
                    getPrevious().setNext(null);
                    getPrevious().updateSnapPosition();
                }
                containerReference.setPrevious(null);
                // disconnect from previous stack
                if (containerReference.stackNumber != -1) {
                    PlayingField.getSTACKS()[containerReference.stackNumber].pop();
                }
                containerReference.changeZOrder(0);
            }
        });

        // drag all following cards
        card.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveFollowing();
            }
        });

        // combine dragged list with the list it is dragged on
        card.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if (card.isAllowDragging()) {

                    // dragging on list
                    int newList = card.mouseInBound() - 4;
                    if (newList >= 0) {
                        // if it's a legal move
                        if (Rules.legalListMove(card, PlayingField.getLISTS()[newList].getLast().getCard())) {
                            // disconnect from previous stack
                            containerReference.stackNumber = -1;
                            // flip next revealed card
                            AbstractCard toFlip = PlayingField.getLISTS()[containerReference.listNumber].getLast().getCard();
                            if (toFlip != null && !toFlip.isRevealed()) {
                                toFlip.flip();
                                toFlip.setAllowDragging(toFlip.isRevealed());
                                toFlip.repaint();
                            }
                            // add card to the chosen list
                            PlayingField.getLISTS()[newList].getLast().setNext(containerReference);
                            // give the moved cards the correct list number
                            for (CardContainer cl = containerReference; cl != null; cl = cl.getNext()) {
                                cl.setListNumber(newList);
                            }
                        }
                        // if it's not a legal move
                        else {
                            // if the card was not from a stack
                            if (stackNumber == -1) {
                                PlayingField.getLISTS()[containerReference.listNumber].getLast().setNext(containerReference);
                            }
                            // if the card was from a stack
                            else {
                                PlayingField.getSTACKS()[containerReference.stackNumber].push(containerReference.getCard());
                                card.setLocation(PlayingField.getSTACKS()[containerReference.stackNumber].getPosition());
                            }
                        }
                        // ??
                        for (Consumer<AbstractCard> listener : listeners) {
                            listener.accept(card);
                        }
                    }

                    // dragging on stack
                    int newStackIndex = card.mouseInBound();
                    if (newStackIndex >= 0 && newStackIndex < 4) {
                        CardStack newStack = PlayingField.getSTACKS()[newStackIndex];
                        AbstractCard top = null;
                        if (newStack.peek() != null)
                            top = newStack.peek();
                        // if it's a legal move
                        if (containerReference.getNext() == null && Rules.legalStackMove(containerReference.getCard(), top)) {
                            // flip next revealed card
                            AbstractCard toFlip = PlayingField.getLISTS()[containerReference.listNumber].getLast().getCard();
                            if (toFlip != null && !toFlip.isRevealed()) {
                                toFlip.flip();
                                toFlip.setAllowDragging(toFlip.isRevealed());
                                toFlip.repaint();
                            }
                            // push the card onto the stack
                            newStack.push(containerReference.getCard());
                            containerReference.stackNumber = newStack.getIndex();
                        }
                        // if it's not a legal move
                        else {
                            // if the card was not from a stack
                            if (stackNumber == -1) {
                                PlayingField.getLISTS()[containerReference.listNumber].getLast().setNext(containerReference);
                            }
                            // if the card was from a stack
                            else {
                                PlayingField.getSTACKS()[containerReference.stackNumber].push(containerReference.getCard());
                                card.setLocation(PlayingField.getSTACKS()[containerReference.stackNumber].getPosition());
                            }
                        }


                    }
                }
                moveFollowing();
                updateSnapPosition();
                changeZOrder(0);
                PlayingField.updateScoreboard();
                if (PlayingField.chekEnd()) {
                    EndFrame endFrame = new EndFrame();
                }

            }
        });
    }

    /**
     * getLast method
     * getter for the last tud.ai2.solitaire.model.cards.CardContainer element which is part of the "linked" card list
     *
     * @return
     */
    public CardContainer getLast() {
        if (getNext() == null)
            return this;
        else
            return getNext().getLast();
    }

    /**
     * hasPrevious method
     * checks if current card container has a previous element
     *
     * @return true if the current card container has previous element, false otherwise
     */
    public boolean hasPrevious() {
        return previous != null;
    }

    /**
     * getPrevious method
     * getter for previous card container element
     *
     * @return tud.ai2.solitaire.model.cards.CardContainer element which is linked to the current one
     */
    public CardContainer getPrevious() {
        return previous;
    }

    /**
     * setPrevious method
     * setter for previous card container element
     *
     * @param previous
     */
    public void setPrevious(CardContainer previous) {
        this.previous = previous;
    }

    /**
     * getCard method
     * returns the current card which is stored in the card attribute
     *
     * @return
     */
    public AbstractCard getCard() {
        return card;
    }

    /**
     * getPosition method
     * getter for the card container object position of the visual representation
     *
     * @return
     */
    public Point getPosition() {
        if (card == null) {
            return initPos;
        } else {
            return card.getLocation();
        }
    }

    /**
     * setCard method
     * setter for the card attribute of the current card container object
     *
     * @param card
     */
    public void setCard(AbstractCard card) {
        this.card = card;
    }

    /**
     * getNext method
     * getter of the next attribute which is a linked card container element
     *
     * @return
     */
    public CardContainer getNext() {
        return next;
    }

    /**
     * hasNext method
     *
     * @return true if the current card container object has a next value, false otherwise
     */
    public boolean hasNext() {
        return next != null;
    }

    /**
     * setNext method
     * connects two card container elements and performs an update of their previous, next and location attributes
     *
     * @param next
     */
    public void setNext(CardContainer next) {
        this.next = next;
        if (next != null) {
            next.setPrevious(this);
            next.setListNumber(this.listNumber);
            next.getCard().setLocation(PlayingField.getPOSITIONS()[this.listNumber + 4]);
        }
        updateSnapPosition();
    }

    /**
     * updateSnapPosition method
     * performs an update on the position attributes of the current card container
     */
    public void updateSnapPosition() {
        Point oldPos = PlayingField.getPOSITIONS()[listNumber + 4];
        Point newPos = PlayingField.getLISTS()[listNumber].getLast().getPosition();
        if (PlayingField.getLISTS()[listNumber].getLast().getCard() != null)
            newPos.setLocation(newPos.x, newPos.y + CARD_OFFSET);

        oldPos.setLocation(newPos);
    }

    /**
     * moveFollowing method
     * performs an update on the position attributes of the next card container
     */
    public void moveFollowing() {
        if (getNext() != null) {
            getNext().getCard().setLocation(getPosition().x, getPosition().y + CARD_OFFSET);
            getNext().moveFollowing();
        }
    }

    /**
     * changeZOrder method
     * changes the z position of this and every following card container to the parameter index
     * choose index = 0 to have this card drawn above every other card
     *
     * @param index
     */
    public void changeZOrder(int index) {
        getCard().getParent().setComponentZOrder(getCard(), index);
        if (getNext() != null) {
            getNext().changeZOrder(index);
        }
        //System.out.println(getCard().string()+ "     " + getCard().getParent().getComponentZOrder(getCard()));
    }


    public static int getCardOffset() {
        return CARD_OFFSET;
    }

    public int getListNumber() {
        return listNumber;
    }

    /**
     * setListNumber method
     * setter for listNumber attribute
     *
     * @param listNumber
     */
    public void setListNumber(int listNumber) {
        this.listNumber = listNumber;
    }

    public int getStackNumber() {
        return stackNumber;
    }

    public void setStackNumber(int stackNumber) {
        this.stackNumber = stackNumber;
    }

    public void addListener(Consumer<AbstractCard> consumer) {
        listeners.add(consumer);
    }

    public void removeListener(Consumer<AbstractCard> consumer) {
        listeners.remove(consumer);
    }

}
