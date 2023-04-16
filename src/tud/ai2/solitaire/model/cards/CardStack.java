package tud.ai2.solitaire.model.cards;

public class CardStack extends AbstractCardStack {

	private static int MAX_STACK = 13;
	private Card[] cardstack= new Card[MAX_STACK];
	private int N=0;

	@Override
	/**
	 * @param Karte cc
	 * Methode um die Karte cc auf den Stack zu legen
	 */
	public void push(AbstractCard cc) {
		// TODO Auto-generated method stub
		if(N<MAX_STACK) {
			cardstack[N]=(Card) cc;
			N++;
		}
	}

	@Override
	/**
	 * @return null wenn keine Karte auf den Stack
	 * @return die oberste Karte von dem Stack, wenn Stack die Karten haben, und nimmt diese Karte, Anzahl von Karte auf den Stack muss eine weniger sein .

	 */
	public AbstractCard pop() {
		// TODO Auto-generated method stub
		if(N==0) {
			return null;

		}
		//cardstack[N]=null;
		AbstractCard a=cardstack[N-1];
		N--;
		return a;


	}

	@Override
	/**
	 * @return die oberste Karte von dem Stack, ohne sie vom Stack zu
entfernen.

	 */
	public AbstractCard peek() {
		// TODO Auto-generated method stub
		if(N==0) return null;
		else{return cardstack[N-1];
		}
	}


	@Override
	/**
	 * @return boolean, ob die Stack leer ist
	 */
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (N==0);
	}

	@Override
	/**
	 * return boolean, ob die Stack voll ist
	 */
	public boolean isFull() {
		// TODO Auto-generated method stub
		return (N==MAX_STACK);
	}

	@Override
	/**
	 * @return Anzahl von Karte vom Stack
	 */
	public int size() {
		// TODO Auto-generated method stub
		return N;
	}

}
