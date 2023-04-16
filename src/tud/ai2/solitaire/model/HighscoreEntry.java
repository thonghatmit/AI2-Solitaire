package tud.ai2.solitaire.model;

/**
 * HighscoreEntry class
 * structure to store all attributes of a player of this game
 * Name which is a string
 * Time which was needed to get that score
 * Score actual score which was achieved during playing
 */
public class HighscoreEntry {
    private final String name;
    private final Integer time;
    private final Integer score;

    //Constructor
    public HighscoreEntry(String receivedName, int receivedTime, int receivedPoints) {
        this.name = receivedName;
        this.time = receivedTime;
        this.score = receivedPoints;
    }


    //Gettermethods
    public String getName() {
        return this.name;
    }

    public Integer getTime() {
        return this.time;
    }

    public Integer getScore() {
        return this.score;
    }

    public String toString() {
        return String.format("%s;%d;%d", getName(), getTime(), getScore());
    }

    public boolean equals(HighscoreEntry e) {
        return name.equals(e.name) && time.equals(e.time) && score.equals(e.score);
    }
}
