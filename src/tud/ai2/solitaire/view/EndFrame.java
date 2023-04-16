package tud.ai2.solitaire.view;

import tud.ai2.solitaire.Launch;
import tud.ai2.solitaire.util.HighscoreListener;

import javax.swing.*;
import java.awt.*;

/**
 * Frame for the end of the Game.
 *
 * @author Kurt Cieslinski
 * @version 1.0
 */
public class EndFrame extends JFrame {
    private JButton newScoreEntryButton;
    private JButton endButton;
    private JButton resetButton;

    private JPanel buttonPane;
    private JPanel labelPane;

    public EndFrame() {
        super("Gewonnen");
        buildFrame();
    }

    private void buildFrame() {
        setSize(360, 150);
        PlayingField.getTimer().stop();
        PlayingField.setTimeDelta((System.currentTimeMillis() - PlayingField.getTimeBegin()) / 1000);
        PlayingField.setEnded(true);


        JLabel textLabel = new JLabel("Du hast gewonnen!");
        JLabel scoreTimeLabel = new JLabel("Du hast " + PlayingField.getTimeDelta() + "s für " + PlayingField.getScore() + " Punkten gebraucht.");


        newScoreEntryButton = new JButton("Punkte speichern");
        newScoreEntryButton.addActionListener(new HighscoreListener());

        endButton = new JButton("Beenden");
        endButton.addActionListener(a -> System.exit(0));

        resetButton = new JButton("Neustart");
        resetButton.addActionListener(a -> {
            setVisible(false);
            dispose();
            Launch.activeField.reset();
        });

        labelPane = new JPanel();
        labelPane.setLayout(new BoxLayout(labelPane, BoxLayout.Y_AXIS));
        labelPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelPane.add(textLabel);
        labelPane.add(scoreTimeLabel);

        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(newScoreEntryButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(endButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(resetButton);

        getContentPane().add(labelPane, BorderLayout.CENTER);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}
