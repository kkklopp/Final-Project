package clockGame;

import Timers.Timer;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScreen extends JPanel implements KeyListener {
    public static Resources loadResources = new Resources();
    private static int screenWidth;
    private static int screenHeight;
    private static int FPS;
    private static final int defaultYspeed = 450; //default player speed
    private static final int defaultXspeed = 35;
    private String startString;
    private int startStringWidth;
    private int recordHighScore;
    private int winCondition;
    private int currentFrame = 0;
    private int overTime;
    private static final Pair defaultAcceleration = new Pair(0, 1500);

    public World gameWorld;
    //private static World gameWorld;
    private Thread gameThread;
    private Timer endGraphicTimer;
    private Timer gameTimer; //static - belongs to GameScreen, not instance of Timers.Timer
    private Timer screenTimer;
    private int gameTime; //referenced in other classes
    private boolean flapWing = true;
    boolean endCredits = false;
    private boolean started;// = false;
    private boolean win = false;// = false; //game reset to not won
    private boolean gameOvertime = false;
    private boolean gameComplete = false;

    public GameScreen(int initWidth, int initHeight, int initFPS, int hammerGoal, int highScore, DisplayGameScreen initGame) {
        screenTimer = new Timer();
        started = false;
        recordHighScore = highScore;
        winCondition = hammerGoal;
        overTime = (80 * winCondition);
        screenWidth = initWidth;
        screenHeight = initHeight;
        FPS = initFPS;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        gameWorld = new World(screenWidth, screenHeight, hammerGoal, highScore, initGame);
        gameThread = new Thread(new GameScreen.Runner());
        gameThread.start();
    }

    class Runner implements Runnable {
        @Override
        public void run() { //LATER: CHANGE SO IT ONLY RUNS UNTIL YOU ACHIEVE MAX NUMBER OF POINTS
            while (gameThread != null && !win && !gameOvertime) {  //if user hasn't won yet and game is not in overtime
                if (started) {
                    gameWorld.updateObjects(1.0 / (double) FPS);
                    if (gameTimer == null) {
                        gameTimer = new Timer();
                    }
                    gameTime = gameTimer.getElapsedTime(); //keeps track of current time in seconds
                    gameTimer.printTime();
                    stopGameOvertime();
                    currentFrame++;
                    win = gameWorld.checkIfWon();
                    if (win || gameOvertime) {
                        endCredits = true;
                    }
                }
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
                // System.out.println("The game loop is running"); //
            }
            while (gameThread != null && endCredits) { //end credits loop
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
            if (win & recordHighScore < winCondition) { //if achieved hammer goal and goal > high score
                getHighScore();
            }
            if (!endCredits) {
                gameComplete = true; //game is complete after end credits are drawn
            }
        }
    }

    public int getHighScore() {
        recordHighScore = winCondition;
        return recordHighScore;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        if (!win && !endCredits) {
            gameWorld.drawObjects(g);
            if (!started) {
                startMessage(g);
            }
            if(started) {
                drawWSAD(g);
            }
        }
        if (endCredits) {
            endGraphic(g);
        }
    }

    public void paintBackground(Graphics g) {
        g.drawImage(loadResources.defaultBackground, 0, 0, screenWidth, screenHeight, null);
    }

    void startMessage(Graphics g) {
        g.setColor(java.awt.Color.BLACK);
        g.fillRect((screenWidth/2) - 230, (screenHeight/2) - 165, 445, 300);
        g.setColor(java.awt.Color.WHITE);
        g.drawRect((screenWidth/2) - 230, (screenHeight/2) - 165, 445, 300);
        g.drawImage(loadResources.wasd, (screenWidth / 2) - 225, (screenHeight / 2) - 150, 450, 300, null);
        g.setColor(loadResources.lightBlue);
        g.setFont(loadResources.Monospaced);
        g.drawString("default speed", (screenWidth / 2)-50, (screenHeight/2) + 80);
        if(screenTimer.getElapsedMilliseconds() - screenTimer.getElapsedTime() > 0.5) {
            g.setFont(loadResources.MonospacedBold);
            startString = "PRESS  ' S '  TO START";
            startStringWidth = g.getFontMetrics().stringWidth(startString);
            g.drawString(startString, ((screenWidth / 2) - (startStringWidth / 2)) + 3, (screenHeight / 2) + 115);
            g.setColor(loadResources.turquoise);
            g.drawString(startString, ((screenWidth / 2) - (startStringWidth / 2)), (screenHeight / 2) + 115);
            g.drawRect((screenWidth/2) - 230, (screenHeight/2) - 165, 445, 300);
        }
    }

    void drawWSAD(Graphics g) {
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(screenWidth - 120, 16, 120, 90);
        g.setColor(loadResources.lightBlue);
        g.drawRect(screenWidth - 120, 16, 120, 90);
        g.setFont(GameScreen.loadResources.Monospaced);
        g.drawImage(loadResources.wasd, screenWidth-114, 20, 112, 75, null);
        g.drawString("default", screenWidth-80, 92);
        g.drawString("speed", screenWidth-80, 102);
       // g.setColor(Color.ORANGE);
        for(int i=0; i<3; i++) {
            g.drawLine(screenWidth - 60 - i, 75, screenWidth - 60 - i, 80);
        }
    }

    static int peekHeight() {
        return screenHeight;
    }

    static int peekWidth() {
        return screenWidth;
    }


    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        //System.out.println("You pressed down: " + c);
        keyChanges(c);
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

    private void keyChanges(char c) {
        switch (c) {
            case 'w':
                gameWorld.setPlayerAcceleration(defaultAcceleration);
                flapWing = !flapWing;
                gameWorld.setPlayerYVelocity(-1 * GameScreen.defaultYspeed);
                break;
            case 's':
                started = true;
                gameWorld.setPlayerAcceleration(defaultAcceleration);
                gameWorld.setPlayerXVelocity(0);
                Pipes.changePipeSpeed(Pipes.getDefaultPipeSpeed());
                break; //horizontal stop
            case 'a':
                gameWorld.setPlayerAcceleration(defaultAcceleration);
                gameWorld.setPlayerXVelocity(-2.2 * GameScreen.defaultXspeed);
                Pipes.changePipeSpeed((int) (0.9 * Pipes.getDefaultPipeSpeed()));
                break;
            case 'd':
                gameWorld.setPlayerAcceleration(defaultAcceleration);
                gameWorld.setPlayerXVelocity(GameScreen.defaultXspeed);
                Pipes.changePipeSpeed((int) (1.4 * Pipes.getDefaultPipeSpeed()));
                break;
        }
    }

    public int getGameTime() {
        return gameTime;
    }

    public void stopGameOvertime() { //stop alarm if game not won yet
        if (gameTimer.getElapsedTime() >= (overTime)) { // if game time is reaches or exceeds 60 seconds per hammer level that user set
            gameOvertime = true;
        }
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void endGraphic(Graphics g) {
        if (endGraphicTimer == null) {
            endGraphicTimer = new Timer();
        }
        g.drawImage(loadResources.background1, 0, 0, 1244, 700, null);
        for (int i = 0; i < 5; i++) {
            g.drawImage(loadResources.grass, 0 + (i * 220), screenHeight - 150, 220, 150, null);
        }
        if (endCredits && win) {
            winCredits(g);
        }
        if (endCredits && !win) {
            loseCredits(g);
        }
    }

    public void winCredits(Graphics g) {
        g.drawImage(loadResources.morning, (screenWidth / 2) - 250, (screenHeight / 2) - 41, 500, 82, null);
        if (endGraphicTimer.getElapsedTime() <= 2) {
            if (endGraphicTimer.getElapsedMilliseconds() - (double) endGraphicTimer.getElapsedTime() < 0.3 || (endGraphicTimer.getElapsedMilliseconds() - (double) endGraphicTimer.getElapsedTime() >0.7 && endGraphicTimer.getElapsedMilliseconds() - (double) endGraphicTimer.getElapsedTime() < 0.8)) {
                g.drawImage(loadResources.clockL, (screenWidth / 2) - 10 + endGraphicTimer.getElapsedTime(), screenHeight - 150, 40, 55, null);
            } else {
                g.drawImage(loadResources.clockR, (screenWidth / 2) - 15 + endGraphicTimer.getElapsedTime(), screenHeight - 150, 40, 55, null);
            }
        } //first five seconds draw clock bouncing left to right

        if (endGraphicTimer.getElapsedTime() > 2 && endGraphicTimer.getElapsedTime() <= 5) {
            if (endGraphicTimer.getElapsedMilliseconds() - (double) endGraphicTimer.getElapsedTime() > 0.5) {
                g.drawImage(loadResources.clockResting, (screenWidth / 2) - 20, screenHeight - 150, 40, 55, null);
            } else {
                g.drawImage(loadResources.clockBlink, (screenWidth / 2) - 20, screenHeight - 150, 40, 55, null);
            }
            g.drawImage(loadResources.hand, (screenWidth / 2) - 25, (screenHeight / 2)+((endGraphicTimer.getElapsedTime() - 3) * 48), 40, 55, null);
        } //next 5 seconds hand turns off alarm

        if (endGraphicTimer.getElapsedTime() > 5 && endGraphicTimer.getElapsedTime() <= 7) {
            g.drawImage(loadResources.clockFlat, (screenWidth / 2) - 20, (screenHeight) - 145, 40, 55, null);
            g.drawImage(loadResources.hand, (screenWidth / 2) - 25, 490, 40, 55, null);
            g.drawImage(loadResources.dust, (screenWidth / 2) - 28, 498, 56, 52, null);
            g.drawImage(loadResources.poof, (screenWidth / 2) - 13, 525, 25, 10, null);
            g.drawImage(loadResources.smoke, (screenWidth / 2) - 270, 300, 500, 500, null);
        } //next 3 seconds alarm is pressed down
        if (endGraphicTimer.getElapsedMilliseconds() > 8.5) {
            endCredits = false;
        }
    }

    public void loseCredits(Graphics g) {
        g.drawImage(loadResources.getUp, (screenWidth / 2) - 300, (screenHeight / 2) - 57, 600, 114, null);
        if (endGraphicTimer.getElapsedTime() <= 3) {
            g.drawImage(loadResources.getUp, (screenWidth / 2) - 300, (screenHeight / 2) - 57, 600, 114, null);
        } else {
            endCredits = false;
        }
    }

    public boolean ifStarted() {
        return started;
    }

    public boolean ifFlap() {
        return flapWing;
    }

    public boolean ifGameComplete() {
        return gameComplete;
    }

}
