package tud.ai2.solitaire.model.cards;

import java.awt.Container;
import java.util.Collection;

public class CardPool extends AbstractCardPool {

	/**
	 * Initializes a tud.ai2.solitaire.model.cards.CardPool instance on its creation.
	 * Initializes the parent class, adds the provided cards to the pool.
	 *
	 * @param cards     a list of cards that will be added to the draw pool
	 * @param container a container to draw on
	 */
	public CardPool(Collection<AbstractCard> cards, Container container) {
		super(cards, container);
		for (AbstractCard card : cards) {
			getDrawStack().push(card);
		}
		prepare();
	}

	@Override
	/*
	 * Methode um den Karte von Zugstapel bis Ablagestapel zu bewegen. Wenn die Zugstapel leer ist, geben null zurück
	 * @return die von Zugstapel zu Ablagestapel bewegte Karte                                                    
	 */
	public AbstractCard drawStackClicked() {
		// TODO Auto-generated method stub
		if(this.getDrawStack().empty()) {
			return null;
		}
		else{
			AbstractCard a=this.getDrawStack().pop();



			this.getWasteStack().push(a);
			return a;

		}


	}


	@Override
	/*
	 * Methode um alle Karte von Ablagestapel zu Zugstapel zu bewegen. Die Ordnung von Zugstapel muss bleiben
	 * 
	 */
	public void redeal() {

		// TODO Auto-generated method stub

		while(!(this.getWasteStack().empty())) {

			AbstractCard a=this.getWasteStack().pop();

			this.getDrawStack().push(a);


		}




	}

	@Override
	/*
	 * Methode um die Karte, die am obersten liegt, von Ablagestapel zu entfernen. Wenn es nicht der Fall ist, oder Ablagestapel leer ist, geben null zurück
	 * @param die oberste von Ablagestapel Karte  
	 */
	public void removeFromWaste(AbstractCard card) {
		// TODO Auto-generated method stub
		if(!(card.equals(null))&&card.equals(this.getWasteStack().peek())&&!(this.getWasteStack().isEmpty())) {

			card=this.getWasteStack().pop();




		}

	}


}
