package tud.ai2.solitaire.util;

import tud.ai2.solitaire.model.cards.CardValue;
import tud.ai2.solitaire.model.cards.Suit;

import java.awt.*;

public abstract class Const {

    //locked card positions
    public final static Point OFFSET = new Point(0, 0);
    public final static Point RESERVE_POS = new Point(20 + OFFSET.x, 20 + OFFSET.y);
    public final static Point DRAW_POS = new Point(85 + OFFSET.x, 20 + OFFSET.y);
    public final static Point FILING_POS = new Point(305 + OFFSET.x, 20 + OFFSET.y);
    public final static int FILING_XOFFSET = 15;
    public final static Point LIST_POS = new Point(20 + OFFSET.x, 120 + OFFSET.y);
    public final static int LIST_XOFFSET = 25;
    public final static String HS_PATH = "assets/highscores.csv";

    public final static int CARD_COUNT = CardValue.values().length * Suit.values().length;

}
