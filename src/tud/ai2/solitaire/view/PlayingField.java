package tud.ai2.solitaire.view;

import tud.ai2.solitaire.Launch;
import tud.ai2.solitaire.model.cards.*;
import tud.ai2.solitaire.util.HighscoreListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static tud.ai2.solitaire.util.Const.*;

/**
 * tud.ai2.solitaire.view.PlayingField class
 * The JFrame that displays the game and all its components.
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow, Kurt Cieslinski)
 * @version 1.0
 */
public class PlayingField extends JFrame {
	//attributes which define the basic frame, where the game is displayed
	private final static int WIDTH = 660;
	private final static int HEIGHT = 500;
	private static BufferedImage placeholder = null;
	private static long timeBegin;
	private static long timeDelta;

	//array of points, which represent corners of the areas where the card elements lie
	private static Point[] POSITIONS;
	private static CardContainer[] LISTS;
	private static CardStack[] STACKS;

	private Container contentPane;
	private Deck deck;
	private static int score;
	private static int redeals;
	private static JLabel scoreBoard = null;
	private static JLabel timeDisplay = null;
	private static JButton highscoreButton;
	private static JButton resetButton;
	private static JButton thangluon;

	private static Timer simpleTimer;
	private static boolean ended;


	/**
	 * tud.ai2.solitaire.view.PlayingField constructor
	 * initializes the frame with given sizes for the initial state
	 * creates a deck object and draws them to be visible
	 *
	 * @param title represent the title of the game, here used to label the frame
	 */
	public PlayingField(String title) {
		// Initialize the JFrame Constructor

		super(title);

		Launch.activeField = this;


		buildFrame();

		setVisible(true);
		repaint();


	}

	/**
	 * constructor only for tests !!! DO NOT USE THIS !!!
	 */
	public PlayingField() {
		buildFrame();

	}

	/**
	 * method to build thr playing field frame
	 */
	private void buildFrame() {
		// save the start time
		timeBegin = System.currentTimeMillis();

		// reset all static elements that could have changed
		resetStaticElements();

		// save the content container for easier access
		contentPane = getContentPane();

		// set the window attributes
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);


		//load the pixelated font
		try {
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/font/pixelated.ttf")));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}

		// create the high score button
		highscoreButton = new JButton("Highscore");
		highscoreButton.setBounds(155 + OFFSET.x, 55 + OFFSET.y, 140, 25);

		highscoreButton.setFont(new Font("pixelated", Font.BOLD, 28));
		contentPane.add(highscoreButton);
		highscoreButton.addActionListener(new HighscoreListener());


		// create the score board label
		scoreBoard = new JLabel("Score: 0");
		scoreBoard.setBounds(155 + OFFSET.x, 10 + OFFSET.y, 140, 25);
		scoreBoard.setFont(new Font("pixelated", Font.BOLD, 30));
		add(scoreBoard);

		// create the time display label
		timeDisplay = new JLabel("Time: 00:00");
		timeDisplay.setBounds(155 + OFFSET.x, 30 + OFFSET.y, 140, 25);
		timeDisplay.setFont(new Font("pixelated", Font.BOLD, 30));
		add(timeDisplay);
		SimpleDateFormat df = new SimpleDateFormat("mm:ss");
		simpleTimer = new Timer(1000, e -> timeDisplay.setText("Time: " + df.format(new Date(System.currentTimeMillis() - timeBegin))));
		simpleTimer.start();

		//TODO task 7
		//----------------------- Reset Button Aufgabe -----------------------------------------------------------------------
		/**
		 * Reset Button hinzufügen, um das Spiel neuzustarten.
		 */
		resetButton= new JButton("Reset");
		contentPane.add(resetButton);

		resetButton.setBounds(200 + OFFSET.x, 90 + OFFSET.y, 100, 25);
		resetButton.setFont(new Font("pixelated", Font.BOLD, 28));

		resetButton.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				reset();

			}
		});





		//----------------------- Ende Reset Button Aufgabe -------------------------------------------------------------------


		// create and draw the initial playing field

		drawField();

		AbstractCard.setSnapPositions(POSITIONS);
		try {
			AbstractCard.setBackImage(ImageIO.read(new File("assets/cards/cardback.png")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		deck = new Deck();

		deck.loadCardImages("assets/cards");

		deck.riffle(20);

		layInitialCards();

		new CardPool(deck.getCards(), getContentPane());
		score = 0;
		redeals = 0;

	}

	/**
	 * addCard method
	 * draws a visual representation of the given card parameter
	 *
	 * @param c
	 */
	public void addCard(AbstractCard c) {
		// c.createPicture();

		contentPane.add(c);
		contentPane.setComponentZOrder(c, 2);
		c.repaint();
	}

	/**
	 * drawField method
	 * creates initial visual elements and boundaries
	 */
	public void drawField() {
		Label emptyLabel = new Label();
		emptyLabel.setVisible(false);
		contentPane.add(emptyLabel);

		try {
			placeholder = ImageIO.read(new File("assets/cards/placeholder.png"));
		} catch (IOException e) {
		}
		for (Point p : POSITIONS) {
			JComponent ph = new JComponent() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(placeholder, 0, 0, 60, 80, null);
				}
			};
			ph.setBounds(p.x, p.y, 60, 80);
			contentPane.add(ph);
		}
		contentPane.repaint();
	}

	/**
	 * layInitialCards method
	 * draws the cards which are elements of the deck object created by the playingfield
	 */
	public void layInitialCards() {

		for (int i = 0; i < 7; i++) {

			AbstractCard card = deck.getCards().remove(0);

			addCard(card);

			LISTS[i].setNext(new CardContainer(card));

			for (int j = 1; j <= i; j++) {
				AbstractCard next = deck.getCards().remove(0);
				LISTS[i].getLast().setNext(new CardContainer(next));
				addCard(next);
			}
			LISTS[i].getLast().getCard().flip();
			LISTS[i].getLast().getCard().setAllowDragging(true);
		}
	}

	/**
	 * getPOSITIONS
	 * getter for the position array of points attribute of the playingfield class
	 *
	 * @return
	 */
	public static Point[] getPOSITIONS() {
		return POSITIONS;
	}

	/**
	 * getLISTS
	 * getter for the list array of card containers attribute of the playingfield class
	 *
	 * @return
	 */
	public static CardContainer[] getLISTS() {
		return LISTS;
	}

	/**
	 * getSTACKS
	 * getter for the array of card stacks attribute of the playingfield class
	 *
	 * @return
	 */
	public static CardStack[] getSTACKS() {
		return STACKS;
	}

	/**
	 * getScore
	 * calculates and returns the score of the current game
	 *
	 * @return the score of the current game
	 */
	public static int getScore() {
		int i = (STACKS[0].size() + STACKS[1].size() + STACKS[2].size() + STACKS[3].size()) * 20;
		i -= redeals * 10;
		//System.out.println(i);
		return i;
	}

	/**
	 * incRedeals
	 * increases the counter for the redeals by one
	 */
	public static void incRedeals() {
		redeals++;
	}

	/**
	 * updateScoreboard
	 * updates the text on the score board label to the proper score
	 */
	public static void updateScoreboard() {
		scoreBoard.setText("Score: " + getScore());
	}

	/**
	 * resetStaticElements
	 * resets all static elements of the playingfield class that could have been changed during the game
	 */
	public static void resetStaticElements() {
		POSITIONS = new Point[]{
				new Point(FILING_POS),
				new Point(FILING_POS.x + 60 + FILING_XOFFSET, FILING_POS.y),
				new Point(FILING_POS.x + 2 * (60 + FILING_XOFFSET), FILING_POS.y),
				new Point(FILING_POS.x + 3 * (60 + FILING_XOFFSET), FILING_POS.y),
				new Point(LIST_POS),
				new Point(LIST_POS.x + 60 + LIST_XOFFSET, LIST_POS.y),
				new Point(LIST_POS.x + 2 * (60 + LIST_XOFFSET), LIST_POS.y),
				new Point(LIST_POS.x + 3 * (60 + LIST_XOFFSET), LIST_POS.y),
				new Point(LIST_POS.x + 4 * (60 + LIST_XOFFSET), LIST_POS.y),
				new Point(LIST_POS.x + 5 * (60 + LIST_XOFFSET), LIST_POS.y),
				new Point(LIST_POS.x + 6 * (60 + LIST_XOFFSET), LIST_POS.y),
		};

		LISTS = new CardContainer[]{
				new CardContainer(0, new Point(POSITIONS[4])), new CardContainer(1, new Point(POSITIONS[5])),
				new CardContainer(2, new Point(POSITIONS[6])), new CardContainer(3, new Point(POSITIONS[7])),
				new CardContainer(4, new Point(POSITIONS[8])), new CardContainer(5, new Point(POSITIONS[9])),
				new CardContainer(6, new Point(POSITIONS[10])),
		};

		AbstractCardStack.resetCount();
		STACKS = new CardStack[]{
				new CardStack(), new CardStack(), new CardStack(), new CardStack()
		};

		score = 0;
		redeals = 0;
	}

	/**
	 * checks if all stacks are full and the game is finished
	 *
	 * @return game is finished
	 */
	public static boolean chekEnd() {
		boolean finish = true;
		for (CardStack cs : STACKS)
			finish &= cs.isFull();
		return finish;
	}


	public static Timer getTimer() {
		return simpleTimer;
	}

	public static long getTimeBegin() {
		return timeBegin;
	}

	public static long getTimeDelta() {
		return timeDelta;
	}

	public static void setTimeDelta(long timeDelta) {
		PlayingField.timeDelta = timeDelta;
	}

	public static boolean isEnded() {
		return ended;
	}

	public static void setEnded(boolean ended) {
		PlayingField.ended = ended;
	}

	/**
	 * method to reset the game
	 */
	public void reset() {
		setVisible(false);
		dispose();
		new PlayingField("Solitaire");
	}
}
