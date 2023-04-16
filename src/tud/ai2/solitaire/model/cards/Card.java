package tud.ai2.solitaire.model.cards;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tud.ai2.solitaire.exceptions.ResourceNotFoundException;

public class Card extends AbstractCard {
	private Suit s;
	private CardValue cv;
	private boolean flip;
	private BufferedImage bi;
	/**
	 * Konstruktor von Karte am Anfang
	 * @param Suit s
	 * @param CardValue cv
	 * @param flip
	 */

	public Card(Suit s, CardValue cv) {
		super();
		this.s=s;
		this.cv=cv;
		flip=false;
	}

	@Override
	/*
	 * Methode zum Vergleich von 2 Karte
	 * @param o
	 * @return boolean true wenn 2 Karte haben gleiche Suit und Value
	 * @return boolean false, wenn andere Falls passieren
	 */
	public boolean equals(Object o) {
		if (!(o instanceof AbstractCard)) {
			return false;
		}
		if (this.s == ((AbstractCard) o).getSuit() && this.cv == ((AbstractCard) o).getValue()) {
			return true;
		}
		else {
			return false;
		}
	}



	@Override
	/*
	 * Methode für Suit nehmen
	 * @return Suit s von Karte
	 */
	public Suit getSuit() {
		// TODO Auto-generated method stub
		return s;
	}


	@Override
	/*
	 * Methode für CardValue nehmen
	 * @return CardValue cv von Karte
	 */
	public CardValue getValue() {
		// TODO Auto-generated method stub
		return cv;
	}

	@Override
	/*
	 * Methode um den Karte aufzudecken
	 * @return Variable flip von Karte
	 */
	public boolean flip() {
		// TODO Auto-generated method stub
		this.flip=!this.flip;
		return flip;
	}
	@Override
	/*
	 * Methode um den Zustand von Karte zu prüfen
	 * @return true wenn die Karte ist aufgedeckt
	 * @return false wenn die Karte ist nicht aufgedeckt
	 */
	public boolean isRevealed() {
		// TODO Auto-generated method stub
		if(flip==true) {

			return true;

		} else {

			return false;
		}
	}


	@Override
	/*
	 * Methode um den Zustand von Karte einzustellen
	 * @param boolean revealed
	 * die Karte ist aufgedeckt, wenn revealed gleich true ist
	 */
	public void setRevealed(boolean revealed) {
		// TODO Auto-generated method stub
		flip=revealed;

	}

	@Override
	/*
	 * Methode um den Bild von Karte einzustellen
	 * @param basePath : Datenlink zur den Bild
	 */
	public void setFrontImage(String basePath) throws ResourceNotFoundException {
		// TODO Auto-generated method stub

		basePath="C:/Users/Thong beo/Desktop/AI2/AI2 Solitaire/assets/cards/";
		String a=this.cv.string().toLowerCase()+".png";
		String b=this.s.string().toLowerCase()+"/";

		File f=new File(basePath+b+a);

		try {
			this.bi=ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ResourceNotFoundException("No resource");
		}
	}

	@Override
	/*
	 * Methode um den Bild von Karte zu nehmen
	 *@return Bild bi von der Karte
	 */
	public BufferedImage getFrontImage() {
		// TODO Auto-generated method stub
		return this.bi;

	}

}
