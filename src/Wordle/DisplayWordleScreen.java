package Wordle;

import javax.swing.*;

public class DisplayWordleScreen {

    public WordleScreen wordleInstance;
    public DisplayWordleScreen (JFrame frame){
        frame = new JFrame("Wordle!!!");
        // JLabel label = new JLabel(icon);
        // frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wordleInstance = new WordleScreen();
        frame.setContentPane(wordleInstance);
        frame.pack();
        frame.setVisible(true);
        //frame.repaint();
    }
}
