package MathAttempt;
import javax.swing.*;
import javax.swing.JPanel;

public class DisplayScreen extends JPanel {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;
    public static final int FPS = 60;
    Thread mathThread;
    boolean check = true;
    JFrame mathFrame;
    public MathScreen mathInstance;
    public DisplayScreen() {
        JFrame mathFrame = new JFrame("Math Problems!!!");
        mathFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //need different exit condition??
        mathInstance = new MathScreen(2);
        mathFrame.setContentPane(mathInstance); //after game is done change content pane???
        mathFrame.setResizable(false);
        mathFrame.pack();
        mathFrame.setVisible(true);
        // frame.add(gameInstance);
        mathFrame.setLocationRelativeTo(null);
    }
}
