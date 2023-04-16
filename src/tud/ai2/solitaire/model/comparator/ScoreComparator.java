package tud.ai2.solitaire.model.comparator;

import java.util.Comparator;

import tud.ai2.solitaire.model.HighscoreEntry;

public class ScoreComparator implements Comparator<HighscoreEntry> {

	@Override

	/**
	 * @param HighscoreEntry a unb zum Vergleich
	 * überschreibende Methode um 2 HighScore zu vergleichen
	 * Wenn 2 Highscore gleich sind, vergleichen wir Namenslänger von Spieler.
	 *@return 1 wenn a oder b null sind oder Score von a großer als b, betrachten wir als großer Fall, 
	 *@return -1 wenn score von a kleiner als score von b 
	 */
	public int compare(HighscoreEntry a, HighscoreEntry b) {
		// TODO Auto-generated method stub
		if(a.getScore()<b.getScore()) {
			return -1;
		} else if(a.getScore()==b.getScore()) {
			return a.getName().compareToIgnoreCase(b.getName());
		}else if(a==null||b==null|| a.getScore()>b.getScore()) {
			return 1;
		}else {
			return 0;
		}
	}

}
