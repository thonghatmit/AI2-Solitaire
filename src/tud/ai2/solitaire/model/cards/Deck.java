package tud.ai2.solitaire.model.cards;

import tud.ai2.solitaire.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static tud.ai2.solitaire.util.Const.CARD_COUNT;

/**
 * Class tud.ai2.solitaire.model.cards.Deck
 * describes a deck object created for the game.
 * An object contains a list of cards, 13x4=52 elements.
 * Those 52 elements represent every possible card value from ace to king exists four times but
 * same values differ by their chosen suit.
 * Therefore no duplicates get created.
 * Decks can shuffle themselves
 *
 * @author (Felix Gail, Philipp Malkmus, Artjom Poljakow)
 * @version 1.0
 */

public class Deck {

	public int cutPoint = -1;
	private List<AbstractCard> cards;
	private static final Random rand = new Random();

	//TODO task 2a)
	/**
	 * Konstruktor von Deck
	 * Alle Karte in der List cards hinzufügen
	 */
	public Deck() {
		cards = new ArrayList<AbstractCard>();

		for(CardValue  cv : CardValue.values()) {
			cards.add(new Card(Suit.CLUBS,cv));
			cards.add(new Card(Suit.DIAMONDS,cv));
			cards.add(new Card(Suit.HEARTS,cv));
			cards.add(new Card(Suit.SPADES,cv));
		}

	}

	//TODO task 2c)

	public void riffle(int iterations) {
		while(iterations!=0) {

			Random r=new Random();
			int a=r.nextInt((31-21)+1)+21;
			List<AbstractCard> left=new ArrayList<>();
			List<AbstractCard> right=new ArrayList<>();
			for(AbstractCard card: cards) {

				if(left.size()<a) {
					left.add(card);


				}else if(left.size()==a) {
					right.add(card);

				}

			}


			cards.removeAll(cards);
			cards.addAll(riffleMerge(left,right));
			iterations=iterations-1;

		}


	}

	//TODO task 2b)
	/**
	 *Methode um die Karten zu mischen
	 *@param Liste von Karte link und Liste von Karte recht
	 *@return Liste von gemischte Karte 
	 */


	public static List<AbstractCard> riffleMerge(List<AbstractCard> left, List<AbstractCard> right) {
		List<AbstractCard>ergebnis = new ArrayList<>();
		Random r=new Random();


		while(ergebnis.size()!=52) {

			double b=r.nextDouble();




			if(left.size()==0 && right.size()!=0) {

				ergebnis.addAll(right);

				break;
			}
			else if(right.size()==0&&left.size()!=0) {

				ergebnis.addAll(left);

				break;
			}


			if(b<=(double)left.size()/((double)left.size()+(double)right.size())) {

				ergebnis.add(left.get(0));
				left.remove(0);
			}
			if(b>(double)left.size()/((double)left.size()+(double)right.size())) {
				ergebnis.add(right.get(0));
				right.remove(0);
			}

		}
		return ergebnis;

	}


	public List<AbstractCard> getCards() {
		return cards;
	}

	//TODO task 6
	/**
	 * Methode um die Bilder für alle Karten hochzuladen
	 * 
	 * @param path Datenlink zur Bilder
	 */
	public void loadCardImages(String path) {
		for(AbstractCard card: cards) {
			try {
				card.setFrontImage(path);

			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(path+"no resource");
			}
		}

	}
}

