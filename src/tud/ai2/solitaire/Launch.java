package tud.ai2.solitaire;

import tud.ai2.solitaire.view.PlayingField;

public class Launch {
    /**
     * main method
     * starts the whole game by creating objects, e.g. the playingfield
     *
     * @param args
     */
    public static PlayingField activeField;

    public static void main(String[] args) {
    	   
        activeField = new PlayingField("Solitaire");
       
        printCredits();
    }

    private static void printCredits() {
        String creditString = "";
        creditString += " -------------------------------- " + System.lineSeparator();
        creditString += " |   Credits Solitaire 2021/22  | " + System.lineSeparator();
        creditString += " |                              | " + System.lineSeparator();
        creditString += " | Urspruengliche Entwickler:   | " + System.lineSeparator();
        creditString += " |  - Felix Gail                | " + System.lineSeparator();
        creditString += " |  - Philipp Malkmus           | " + System.lineSeparator();
        creditString += " |  - Artjom Poljakow           | " + System.lineSeparator();
        creditString += " |                              | " + System.lineSeparator();
        creditString += " | Entwickler:                  | " + System.lineSeparator();
        creditString += " |  - Robert Cieslinski         | " + System.lineSeparator();
        creditString += " |  - Kurt Cieslinski           | " + System.lineSeparator();
        creditString += " |                              | " + System.lineSeparator();
        creditString += " -------------------------------- " + System.lineSeparator();
        System.err.println(creditString);
    }
}
