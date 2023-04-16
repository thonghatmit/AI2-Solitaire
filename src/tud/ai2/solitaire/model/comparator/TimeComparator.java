package tud.ai2.solitaire.model.comparator;

import java.util.Comparator;

import tud.ai2.solitaire.model.HighscoreEntry;
import tud.ai2.solitaire.view.HighScoreFrame;

public class TimeComparator implements Comparator<HighscoreEntry> {

	@Override
	public int compare(HighscoreEntry a, HighscoreEntry b) {
		/**
		 * Methode um Spielzeit von Spieler zu vergleichen
		 * @return -1 wenn score von a kleiner als b
		 * @return 1 wenn score von a groﬂer als b oder a oder b null sind
		 * wenn Spielzeit von a gleich Spielzeit von b, vergleichen wir ihre Namensl‰nge
		 */
		// TODO Auto-generated method stub
		if(a.getTime()<b.getTime()) {
			return -1;
		} else if(a.getTime()==b.getTime()) {
			return a.getName().compareToIgnoreCase(b.getName());
		}else if(a==null||b==null|| a.getTime()>b.getTime()) {
			return 1;
		}else {
			return 0;
		}
	}


}
