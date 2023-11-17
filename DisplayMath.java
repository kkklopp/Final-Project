import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
public class DisplayMath{
    public DisplayMath(){

        JFrame frame = new JFrame("Math Problems!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MathProblems mainInstance = new MathProblems(2);
        frame.setContentPane(mainInstance);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}