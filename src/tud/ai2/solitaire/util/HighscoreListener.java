package tud.ai2.solitaire.util;

import tud.ai2.solitaire.view.HighScoreFrame;
import tud.ai2.solitaire.view.PlayingField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HighscoreListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!HighScoreFrame.isHighScoreShown()) {
            HighScoreFrame frame = new HighScoreFrame();
            frame.readDatafile(Const.HS_PATH);
            frame.createWindow();
            frame.setVisible(true);
            HighScoreFrame.showHighscore(true);
            if (!PlayingField.isEnded())
                PlayingField.setTimeDelta((System.currentTimeMillis() - PlayingField.getTimeBegin()) / 1000);
            HighScoreFrame.setTime((int) PlayingField.getTimeDelta());
        } else {
            System.out.println("Highscore is already shown.");
        }
    }

}
