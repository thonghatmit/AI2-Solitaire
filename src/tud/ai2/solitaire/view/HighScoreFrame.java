package tud.ai2.solitaire.view;

import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdIn;
import tud.ai2.solitaire.model.HighscoreEntry;
import tud.ai2.solitaire.model.comparator.ScoreComparator;
import tud.ai2.solitaire.model.comparator.TimeComparator;
import tud.ai2.solitaire.util.Const;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HighScoreFrame extends JFrame{
	private static boolean showHighscore = false;
	private List<HighscoreEntry> highScoreEntries;
	private JTable highscoreTable;
	private static DefaultTableModel model;
	private static JTextField nameField;

	public static int timePlayed;

	//visualization of table content
	private static final String[] colums = {"Name", "Time", "Score"};

	/**
	 * constructor for HighScoreFrame
	 */
	public HighScoreFrame() {
		super("Highscore");
		highScoreEntries = new ArrayList<>();

		model = new DefaultTableModel(feedingShowingTable(highScoreEntries), colums);


	}

	/**
	 * method to create a HighscoreFrame
	 */
	public void createWindow() {

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		//closing the windows triggers method for writing content to file
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				showHighscore(false);
				writeDataFile();
				SecurityManager security = System.getSecurityManager();
				if (security != null) {
					security.checkExit(0);
				}
			}
		});
		setSize(300, 300);
		getContentPane().setLayout(new BorderLayout());

		highscoreTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(highscoreTable);
		highscoreTable.setFillsViewportHeight(true);//resize the table to viewable area

		highscoreTable.getTableHeader().addMouseListener(new MouseAdapter() {


			public void mouseClicked(MouseEvent e) {

				int col = highscoreTable.columnAtPoint(e.getPoint());

				//TODO begin task 5b)
		//----------------------- Comparator Aufgabe -----------------------------------------------------------------------
				/**
				 * Sortieren Algorithmus von List HighscoreEntries
				 * highscore absteigen
				 * Spielzeit aufsteigen
				 * Merge Sort
				 * wenn col==1, sortieren wir Spielzeit
				 * wenn col==2, sortieren wir Score
				 */
				TimeComparator t= new TimeComparator();
				ScoreComparator s=new ScoreComparator();
				if(col==1) {
					for(int i=0;i<highScoreEntries.size();i++) {
						for(int j=0;j< highScoreEntries.size()-1;j++) {
							if(t.compare(highScoreEntries.get(j), highScoreEntries.get(j+1))>0) {
								Object g=highScoreEntries.get(j);
								highScoreEntries.set(j, highScoreEntries.get(j+1));
								highScoreEntries.set(j+1, (HighscoreEntry) g);

							}
						}
					}
				}
				if(col==2) {
					for(int i=0;i<  highScoreEntries.size();i++) {
						for(int j=0;j< highScoreEntries.size()-1;j++) {
							if(s.compare(highScoreEntries.get(j), highScoreEntries.get(j+1))<0) {
								Object g=highScoreEntries.get(j);
								highScoreEntries.set(j, highScoreEntries.get(j+1));
								highScoreEntries.set(j+1, (HighscoreEntry) g);

							}
						}
					}
				}

				//----------------------- Ende Comparator Aufgabe -------------------------------------------------------------------
				model.setDataVector(feedingShowingTable(highScoreEntries), colums);
			}
		});
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		nameField = new JTextField("Enter your name and hit enter", 15);
		getContentPane().add(nameField, BorderLayout.SOUTH);
		nameField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					includePlayer();
				}
			}

		});
		model.setDataVector(feedingShowingTable(highScoreEntries), colums);
		setVisible(false);
	}

	//read file leant on to reference, no exceptions
	public void readDatafile(String path) {
		highScoreEntries = new ArrayList<>();

		//some kind of errorhandling
		if (new File(path).exists()) {
			StdIn.setStdInToFile(path);
		} else {
			System.err.println("File " + path + " not found.");
			return;
		}

		HighscoreEntry appendingElement;
		String line;
		line = StdIn.readLine();

		while (line != null) {
			String[] split = line.split(";");
			appendingElement = new HighscoreEntry(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			highScoreEntries.add(appendingElement);
			line = StdIn.readLine();
		}
		StdIn.resetStdIn();
	}

	//write file leant on to reference, no exceptions
	public void writeDataFile() {
		//some kind of errorhandling
		if ((!new File(Const.HS_PATH).exists())) {
			System.err.println("File " + Const.HS_PATH + " is not found. New file will be created.");
		}

		Out out;
		out = new Out(Const.HS_PATH);

		for (HighscoreEntry entry : highScoreEntries) {
			out.println(entry.toString());
		}
		out.close();
	}

	//DataTableModel expects Object[][]
	private static Object[][] feedingShowingTable(List<HighscoreEntry> someWaySorted) {
		Object[][] twoDimensionalArray = new Object[someWaySorted.size()][3];
		for (int i = 0; i < someWaySorted.size(); i++) {
			twoDimensionalArray[i][0] = someWaySorted.get(i).getName();
			twoDimensionalArray[i][1] = someWaySorted.get(i).getTime();
			twoDimensionalArray[i][2] = someWaySorted.get(i).getScore();
		}
		return twoDimensionalArray;
	}

	//include current player to the list
	private void includePlayer() {
		HighscoreEntry appendingElement = new HighscoreEntry(nameField.getText(), timePlayed, PlayingField.getScore());
		boolean contains = false;
		for (HighscoreEntry entry : highScoreEntries) {
			if (entry.equals(appendingElement)) {
				contains = true;
				System.out.println("You are already part of the list.");
			}
		}

		//only add if not already contains same element
		if (!contains) {
			highScoreEntries.add(appendingElement);
		}
		model.setDataVector(feedingShowingTable(highScoreEntries), colums);
	}

	public List<HighscoreEntry> getEntries() {
		return highScoreEntries;
	}

	public void setEntries(ArrayList<HighscoreEntry> entries) {
		this.highScoreEntries = entries;
	}

	public static boolean isHighScoreShown() {
		return showHighscore;
	}

	public static void showHighscore(boolean show) {
		showHighscore = show;
	}

	public static void setTime(int timeElapsed) {
		timePlayed = timeElapsed;
	}
}
