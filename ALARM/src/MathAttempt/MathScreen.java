package MathAttempt;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.PrintStream;
import java.awt.event.WindowEvent;

public class MathScreen extends JPanel implements KeyListener{

    static MathWorld mathWorld;
    Thread mathThread;
    int WIDTH = 900;
    int HEIGHT = 700;
    int FPS = 60;
    String mathSign = "+";
    String userAnswer = "";
    boolean guessed = false;
    public boolean complete = false;
    int argNum;

    public MathScreen(int initInt) {
        argNum = initInt;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        mathWorld = new MathWorld(argNum);
        mathThread = new Thread(new MathScreen.Runner());
        mathThread.start();
    }

    class Runner implements Runnable {
        @Override
        public void run() { //LATER: CHANGE SO IT ONLY RUNS UNTIL YOU ACHIEVE MAX NUMBER OF POINTS
            while(mathThread != null && !complete) {
                //mathWorld.updateObjects(1.0 / (double) FPS);
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        mathWorld.displayMathProblem(g, mathSign);
        g.drawString(userAnswer,50,50);

        if(guessed) {
            if (MathWorld.correct) {
                paintBackground(g);
                g.drawString("Correct!", 100, 100);
                complete = true;
                g.drawString("Press ESC to Turn Off Alarm", 10, 400);
            } else {
                paintBackground(g);
                g.drawString("WRONG!", 100, 100);
                g.drawString("Press C to Try Again", 100, 400);
            }
        }
    }

    public void paintBackground(Graphics g) {
        g.setColor(java.awt.Color.MAGENTA);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(java.awt.Color.YELLOW);
    }


    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();

        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            mathWorld.checkAnswer(Integer.parseInt(userAnswer)); guessed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && userAnswer.length() > 0) {
            userAnswer = userAnswer.substring(0,userAnswer.length()-1);
        }
        if(Character.getNumericValue(c)>=0 && Character.getNumericValue(c)<=9) {
            userAnswer+=c;
        }

        if(c == 'c' && guessed == true && MathWorld.correct == false) {
            mathWorld = new MathWorld(argNum);
            guessed = false;
            userAnswer = "";
        }
        if(e.getKeyCode() ==KeyEvent.VK_ESCAPE && guessed == true && MathWorld.correct == true) {
            System.exit(0);
        }

    }

    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
    }


    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

}
