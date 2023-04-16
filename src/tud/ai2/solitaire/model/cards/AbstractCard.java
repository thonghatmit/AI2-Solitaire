package tud.ai2.solitaire.model.cards;

import tud.ai2.solitaire.exceptions.ResourceNotFoundException;
import tud.ai2.solitaire.view.PlayingField;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * Class tud.ai2.solitaire.model.cards.AbstractCard
 * defines an abstract class which contains most attributes and declarations.
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow)
 * @version 1.0
 */

public abstract class AbstractCard extends JComponent {

    //attributes to define the visual frame and interaction by mouse
    private final static int preferredWidth = 60;
    private final static int preferredHeight = 80;
    private final static int minWidth = 60;
    private final static int minHeight = 80;
    private static BufferedImage cardback;
    private static Point[] snapPositions;


    private Point temp_ObjPos;
    private Point temp_MousePos;
    private boolean allowDragging;
    private boolean allowFlipping;

    //constructor for tud.ai2.solitaire.model.cards.AbstractCard containing methods for visual representation
    public AbstractCard() {
        setSize(getPreferredSize());
        AbstractCard cardRef = this;

        // Flip
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Click");
                if (allowFlipping) {
                    flip();
                    setAllowDragging(isRevealed());
                    cardRef.repaint();
                }
            }
        });

        // Save current positions
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //System.out.println("Pressed");
                if (allowDragging) {
                    temp_MousePos = e.getLocationOnScreen();
                    temp_ObjPos = getLocation();
                    getParent().setComponentZOrder(cardRef, 0);
                }
            }
        });

        // Drag the card
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                //System.out.println("Dragged");
                if (allowDragging) {
                    Point currPos = e.getLocationOnScreen();
                    setLocation(
                            temp_ObjPos.x + (currPos.x - temp_MousePos.x),
                            temp_ObjPos.y + (currPos.y - temp_MousePos.y));
                }
            }
        });

        // Snap to position
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if (allowDragging) {
                    getParent().setComponentZOrder(cardRef, 0);
                    setLocation(mouseInBound() != -1 ? PlayingField.getPOSITIONS()[mouseInBound()] : temp_ObjPos);
                }
            }
        });
    }

    /**getSuit method
     * shall return the actual suit attribute of this card->getter
     * changing this value is not intended
     *
     * @return tud.ai2.solitaire.model.cards.Suit of the tud.ai2.solitaire.model.cards.Card, shall be one of those declared in the suit enum
     */
    public abstract Suit getSuit();

    /**getValue method
     * shall return the actual value of this card->getter
     * changing this value is not intended
     *
     * @return value of the tud.ai2.solitaire.model.cards.Card, shall be one of those declared in the card value enum
     */
    public abstract CardValue getValue();

    /**flip method
     * Flip the card.
     *
     * @return true if the card is showing its value, false if it is upside down
     */
    public abstract boolean flip();

    /**isRevealed method
     * method to check visibility of this certain card->getter
     *
     * @return true if the value is shown to the player; false otherwise
     */
    public abstract boolean isRevealed();

    /**setRevealed method
     * Set the direction the card is facing.->setter
     *
     * @param revealed wether the card should show its value (<b>true</b>) or its backside (<b>false</b>);
     */
    public abstract void setRevealed(boolean revealed);

    /**isAllowDragging method
     * method to get allowDragging attribute.->getter
     *
     * @return true if the current card is draggable else false
     */

    public final boolean isAllowDragging() {
        return allowDragging;
    }

    /**setAllowDragging method
     * method to setAllowDragging boolean
     * e.g. card which are revealed to the player should be draggable
     * cards which are not yet revealed and/or lie in a certain order in the stack should not be.
     *
     * @param allowDragging is true if this card should be draggable false otherwise
     */

    public final void setAllowDragging(boolean allowDragging) {
        this.allowDragging = allowDragging;
    }

    /**isAllowFlipping method
     * method to get allowFlipping boolean.->getter
     * it indicates if this certain card is flippable or not
     *
     * @return true if the card is flippable otherwise false
     */
    public final boolean isAllowFlipping() {
        return allowFlipping;
    }

    /**setAllowFlipping method
     * method to set allowFlipping boolean.->setter
     * allows flipping for player during game or denies
     *
     * @param allowFlipping
     */
    public final void setAllowFlipping(boolean allowFlipping) {
        this.allowFlipping = allowFlipping;
    }

    /**getSnapPositions method
     * return an array of points which represent the coordinates where the card connect to other cards
     *
     * @return array of points
     */
    public static Point[] getSnapPositions() {
        return snapPositions;
    }

    /**setSnapPositions method
     * stores the positions where the current card connects to their neighbors
     *
     * @param snapPositions
     */
    public static void setSnapPositions(Point[] snapPositions) {
        AbstractCard.snapPositions = snapPositions;
    }


    /**
     * Returns the string representation for a card
     */
    public final String string() {
        return String.format("%s of %s", getValue().string(), getSuit().name());
    }

    /**
     * Loads an image that can be displayed as the card face.
     *
     * @param basePath Base path to the card assets. It is to expect, that the card images can be found under
     *             &lt;Suite&gt;/&lt;tud.ai2.solitaire.model.cards.CardValue&gt;.png (all lowercase)
     * @throws ResourceNotFoundException when the expected resource file can not be found
     */
    public abstract void setFrontImage(String basePath) throws ResourceNotFoundException;

    /**
     * Returns the front image of the card, or <em>null</em> if none is set.
     */
    public abstract BufferedImage getFrontImage();

    /**getBackImage method
     * returns the cardback attribute of this card, which is a BufferedImage for visual representation
     *
     * @return the back of this specific card
     */
    public static BufferedImage getBackImage() {
        return cardback;
    }

    /**setBackImage method
     * setter for the cardback attribute, it needs a visual represenation of the back
     *
     * @param image is the desired back of this card
     */
    public static void setBackImage(BufferedImage image) {
        cardback = image;
    }

    /**getImage method
     * this is actually used during the game to get the current valid visual represenation of the card
     *
     * @return is always an BufferedImage, depends if the card shows its front or back
     */
    public final BufferedImage getImage() {
        if (isRevealed()) {
            return getFrontImage();
        }
        return getBackImage();
    }

    /**getPreferredSize method
     * getter for preferred size attributes stored in an dimension structure
     *
     * @return returns a dimension structure which contains width and size
     */
    @Override
    public final Dimension getPreferredSize() {
        return new Dimension(preferredWidth, preferredHeight);
    }

    /**getMinimumSize method
     * getter for minimum size attributes stored in an dimension structure
     *
     * @return returns a dimension structure which contains width and size
     */
    @Override
    public final Dimension getMinimumSize() {
        return new Dimension(minWidth, minHeight);
    }

    /**paintComponent method
     * actual method for visual representation of this card considering the images loaded and sizes given
     *
     * @param g
     */
    @Override
    public final void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        super.paintComponent(g);
        if (getImage() != null) {
            g.drawImage(getImage(), 0, 0, width, height, null);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            if (isRevealed()) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(0, 0, width, height);
                g2.setColor(getSuit() == Suit.DIAMONDS || getSuit() == Suit.HEARTS ? Color.RED : Color.BLACK);
                g2.setFont(g2.getFont().deriveFont(8f));
                g2.drawString(string(), 2, 12);
            } else {
                g2.setColor(Color.GRAY);
                g2.fillRect(0, 0, width, height);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(1, 1, width - 2, height - 2);
        }
    }

    /**
     * Checks in which of the 13 positions on the field the middle of the held card
     * (or first card of the held card list) currently is
     *
     * @return the number of the position the card is in. -1 if the card is outside
     * of every position
     */
    public final int mouseInBound() {
        int middleX = getX() + 30;
        int middleY = getY() + 40;
        for (int i = 0; i < 11; i++) {
            Point p = PlayingField.getPOSITIONS()[i];
            boolean inBounds = (middleX >= p.x && middleX <= p.x + 60
                    && middleY >= p.y && middleY <= p.y + 80);
            if (inBounds)
                return i;
        }
        return -1;
    }
}

