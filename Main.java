import javax.swing.*;

public class Main {
    public Main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Alarm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Alarm alarm = new Alarm();

            frame.setContentPane(alarm.getPanel());
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
