package tud.ai2.solitaire.model.cards;

import tud.ai2.solitaire.model.Rules;
import tud.ai2.solitaire.view.EndFrame;
import tud.ai2.solitaire.view.PlayingField;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Stack;

import static tud.ai2.solitaire.util.Const.DRAW_POS;
import static tud.ai2.solitaire.util.Const.RESERVE_POS;

/**
 * tud.ai2.solitaire.model.cards.AbstractCardPool class
 * contains the implementation of the card pool which is available in the upper left corner of the game
 * this pool contains cards which the player is able to browse during the game and take cards from
 * cards from those piles can be moved to add to fulfill the game task
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow, Kurt Cieslinski)
 * @version 1.0
 */

public abstract class AbstractCardPool {

    //two stacks which exchange cards while they get drawn
    private final Stack<AbstractCard> drawStack;
    private final Stack<AbstractCard> wasteStack;
    private final CardPoolClickListener clickListener;
    private final CardPoolReleaseListener releaseListener;
    private final static String REDEAL_PATH = "assets/cards/redeal.png";

    /**
     * tud.ai2.solitaire.model.cards.AbstractCardPool constructor
     * which gets a collection of cards and an awtContainer
     * the awtContainer visualizes all elements (cards) from the first parameter, therefore the drawCards
     *
     * @param drawCards
     * @param awtContainer
     */

    public AbstractCardPool(Collection<AbstractCard> drawCards, Container awtContainer) {
        clickListener = new CardPoolClickListener();
        releaseListener = new CardPoolReleaseListener();
        wasteStack = new Stack<>();
        drawStack = new Stack<>();
        Redeal redeal = new Redeal();
        try {
            redeal.setImage(ImageIO.read(new File(REDEAL_PATH)));
        } catch (IOException e) {
            System.out.printf("Couldnt find file '%s'. Using square as Redeal Component", REDEAL_PATH);
            redeal.setForeground(Color.DARK_GRAY);
            redeal.setBorder(new LineBorder(Color.RED, 2));
        }
        redeal.setLocation(RESERVE_POS);
        redeal.setSize(60, 80);
        redeal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                prepare();
                PlayingField.updateScoreboard();
            }
        });
        if (awtContainer != null) {
            drawCards.forEach(awtContainer::add);
            awtContainer.add(redeal);
        }
    }

    /**
     * CardPoolClickListener private class
     * inner class which gets used by cardpool objects
     * this overrides the existent mouseClicked method,
     * to enable exchange ob card elements of the drawStack and wasteStack by mouseclick events
     */
    private class CardPoolClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            AbstractCard card = drawStackClicked();
            if (card.equals(event.getComponent())) {
                card.removeMouseListener(clickListener);
                card.setRevealed(true);
                card.setLocation(DRAW_POS);
                card.setAllowDragging(true);
                card.getParent().setComponentZOrder(card, 0);
                card.repaint();
                if (!drawStack.isEmpty()) {
                    markAsTopStack(drawStack.peek());
                }
            } else {
                throw new RuntimeException("Clicked Component does not match top card on stack.");
            }
        }
    }

    /**
     * CardPoolReleaseListener private class
     * inner class which gets used by cardpool objects
     * this overrides the existent mouseReleased method,
     * while releasing the event(mouseClicke) the overridden method checks for legal operations
     * e.g. the current position, for valid positions the dragged card gets included to the list below(in bound area)
     * if the position is not valid the content is relocated on origin spot
     */
    private class CardPoolReleaseListener extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent event) {
            AbstractCard thisCard = ((AbstractCard) event.getSource());
            int bound = thisCard.mouseInBound();
            // If the card is drawn onto a list
            if (bound - 4 >= 0) {
                if (Rules.legalListMove(thisCard, PlayingField.getLISTS()[bound - 4].getLast().getCard())) {
                    thisCard.removeMouseListener(releaseListener);
                    CardContainer cardContainer = new CardContainer(thisCard);
                    PlayingField.getLISTS()[bound - 4].getLast().setNext(cardContainer);
                    //cardContainer.addListener(tud.ai2.solitaire.model.cards.AbstractCardPool.this::removeFromWaste);
                    removeFromWaste(thisCard);
                    cardContainer.changeZOrder(0);
                } else {
                    thisCard.setLocation(DRAW_POS);
                    thisCard.repaint();
                }
            }

            // If the card is drawn onto a stack
            if (bound >= 0 && bound < 4) {
                AbstractCard top = null;
                if (PlayingField.getSTACKS()[bound].peek() != null)
                    top = PlayingField.getSTACKS()[bound].peek();
                if (Rules.legalStackMove(thisCard, top)) {
                    thisCard.removeMouseListener(releaseListener);
                    CardContainer cardContainer = new CardContainer(thisCard);
                    PlayingField.getSTACKS()[bound].push(cardContainer.getCard());
                    cardContainer.setStackNumber(PlayingField.getSTACKS()[bound].getIndex());
                    //cardContainer.addListener(tud.ai2.solitaire.model.cards.AbstractCardPool.this::removeFromWaste);
                    removeFromWaste(thisCard);
                    cardContainer.changeZOrder(0);
                } else {
                    thisCard.setLocation(DRAW_POS);
                    thisCard.repaint();
                }
            }
            PlayingField.updateScoreboard();
            if (PlayingField.chekEnd()) {
                EndFrame endFrame = new EndFrame();
            }
        }
    }

    /**
     * prepare method
     * preparation for correct visual representation of the underlying stack structures
     */
    protected final void prepare() {
        redeal();
        PlayingField.incRedeals();
        drawStack.forEach(card -> {
            card.setVisible(false);
            card.setRevealed(false);
            card.setAllowDragging(false);
            card.setAllowFlipping(false);
            card.removeMouseListener(clickListener);
            card.removeMouseListener(releaseListener);
        });
        if (!drawStack.isEmpty()) {
            markAsTopStack(drawStack.peek());
        }
    }

    /**
     * markAsTopStack method
     * creating click events for the card which gets exchanged from drawStack to wasteStack
     *
     * @param card
     */
    protected final void markAsTopStack(AbstractCard card) {
        card.addMouseListener(clickListener);
        card.addMouseListener(releaseListener);
        card.setLocation(RESERVE_POS);
        card.setVisible(true);
        card.repaint();
    }

    public final Stack<AbstractCard> getDrawStack() {
        return drawStack;
    }

    public final Stack<AbstractCard> getWasteStack() {
        return wasteStack;
    }

    /**
     * Redeal class
     * extends the JComponent by "overriding" paintComponent method,
     * by changing to the required sizes(width, height)
     */
    public static class Redeal extends JComponent {
        private BufferedImage image;

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (image != null) {
                graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    /**
     * Called when the draw stack is clicked. Moves a card from the draw stack to the waste stack
     *
     * @return The card moved from the draw stack to the waste stack
     */
    public abstract AbstractCard drawStackClicked();

    /**
     * Moves all the cards from the waste stack back to the draw stack. The order of the draw stack has to stay the same
     * with every redeal
     */
    public abstract void redeal();

    /**
     * This method is called when a card is removed from the tud.ai2.solitaire.model.cards.CardPool. If the waste stack is empty, or the {@code card}
     * handed over to this method is not on top of the stack, this method call is to be ignored.
     *
     * @param card card that is expected at the top of the waste stack
     */
    public abstract void removeFromWaste(AbstractCard card);
}
