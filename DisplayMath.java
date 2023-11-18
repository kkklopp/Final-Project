import javax.swing.*;

public class DisplayMath {
    public DisplayMath() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Math Problems!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            Alarm alarmInstance = new Alarm();
            MathProblems mathProblems = new MathProblems(2, alarmInstance);

            frame.setContentPane(mathProblems);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }

    public static void main(String[] args) {
        new DisplayMath();
    }
}
