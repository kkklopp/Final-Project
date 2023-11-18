import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MathProblems extends JPanel {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;
    public static final int FPS = 60;

    private int[] randomNumsArray;
    private boolean mathProblemDisplayed = false;
    private Timer timer;
    private Alarm alarmInstance;

    public MathProblems(int n, Alarm alarmInstance) {
        randomNumsArray = new int[n];
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        generateRandom();
        this.alarmInstance = alarmInstance;

        timer = new Timer(1000 / FPS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mathProblemDisplayed) {
                    displayMathProblem();
                }
            }
        });
        timer.start();
    }

    public void generateRandom() {
        Random rand = new Random();
        for (int i = 0; i < randomNumsArray.length; i++) {
            randomNumsArray[i] = rand.nextInt(100);
        }
    }

    public int multiply() {
        int product = 1;
        for (int i = 0; i < randomNumsArray.length; i++) {
            product *= randomNumsArray[i];
        }
        return product;
    }

    public boolean check_answer(int answer) {
        if (answer == this.multiply()) {
            alarmInstance.stopAlarm();
            return true;
        }
        return false;
    }

    public void displayMathProblem() {
        mathProblemDisplayed = true;
        repaint();

        String answer = JOptionPane.showInputDialog("Enter the product of the numbers:");
        if (answer != null) {
            try {
                int userAnswer = Integer.parseInt(answer);
                if (check_answer(userAnswer)) {
                    mathProblemDisplayed = false;
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect answer. Try again.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 50);
        g.setColor(Color.ORANGE);
        g.setFont(font);
        g.drawString(Integer.toString(randomNumsArray[0]), 230, 350);
        g.drawString("X", 490, 350);
        g.drawString(Integer.toString(randomNumsArray[1]), 750, 350);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Math Problems");
                Alarm alarm = new Alarm(); 
                MathProblems mathProblems = new MathProblems(2, alarm);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(mathProblems);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
