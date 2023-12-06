package clockGame;

import javax.swing.*;

public class DisplayGameScreen {
    public GameScreen gameInstance;
    //public JFrame frame;

    public DisplayGameScreen(int initWidth, int initHeight, int initFPS, int hammerGoal, int highScore, JFrame frame) {
        //frame = new JFrame("Alarm Challenge!!!");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameInstance = new GameScreen(initWidth, initHeight, initFPS, hammerGoal, highScore, this);
        frame.setContentPane(gameInstance); //after game is done change content pane???
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}
