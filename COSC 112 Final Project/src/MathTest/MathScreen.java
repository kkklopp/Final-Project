package MathTest;
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
    int width;
    int height ;
    int fps;
    String mathSign = "+";
    String userAnswer = "";
    boolean guessed = false;
    public boolean complete = false;
    int argNum;
    int [] storeProblem;

    public MathScreen(int initArg, int initWidth, int initHeight, int initFPS) {
        argNum = initArg;
        width = initWidth;
        height = initHeight;
        storeProblem = new int [argNum];
        fps = initFPS;
        this.setPreferredSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        mathWorld = new MathWorld(argNum);
        mathThread = new Thread(new MathScreen.Runner());
        mathThread.start();
    }

    class Runner implements Runnable {
        @Override
        public void run() { //LATER: CHANGE SO IT RUNS UNTIL YOU ACHIEVE MAX NUMBER OF POINTS
            while(mathThread != null && !complete) { //ADD : AND WHILE x SCORE IS LESS THAN GOAL SCORE
                //mathWorld.updateObjects(1.0 / (double) FPS);
                repaint();
                try {
                    Thread.sleep(1000 / fps);
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
                g.drawString("Press 'ESC' to Turn Off Alarm", 10, 350);
            } else {
                paintBackground(g);
                g.drawString("WRONG!", 100, 100);
                g.drawString("Press 'N' for a New Problem", 100, 300);
                g.drawString("Or Press 'C' to Try Again", 100, 400);
            }
        }
    }

    public void paintBackground(Graphics g) {
        g.setColor(java.awt.Color.MAGENTA);
        g.fillRect(0,0,width,height);
        g.setColor(java.awt.Color.YELLOW);
    }


    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();

        if(e.getKeyCode() == KeyEvent.VK_ENTER && userAnswer!="") { //if user presses enter
            mathWorld.checkAnswer(Integer.parseInt(userAnswer)); guessed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && userAnswer.length() > 0) {
            userAnswer = userAnswer.substring(0,userAnswer.length()-1);
        }
        if(Character.getNumericValue(c)>=0 && Character.getNumericValue(c)<=9) {
            userAnswer+=c;
        }

        if(c == 'n' && guessed == true && MathWorld.correct == false) {
            mathWorld = new MathWorld(argNum);
            guessed = false;
            userAnswer = "";
        }
        if(c == 'c' && guessed == true && MathWorld.correct == false) {
            storeProblem = mathWorld. getRanNumsArray();
            mathWorld = new MathWorld(argNum);
            mathWorld.setNumArray(storeProblem);
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
