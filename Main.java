import clockGame.GameScreen;

import javax.swing.JFrame;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Alarm Challenge!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameScreen gameInstance = new GameScreen();
        frame.setContentPane(gameInstance); //after game is done change content pane???
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        // frame.add(gameInstance);
        frame.setLocationRelativeTo(null);
    }
}