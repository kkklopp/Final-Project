package clockGame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScreen extends JPanel implements KeyListener{
    static Resources loadResources = new Resources();
    //Set screen settings
    private static final int screenWidth =900;

    private static final int screenHeight = 700;
    final static int FPS = 60;
    static final int defaultYspeed = 450;
    static final int defaultXspeed = 20;
    static final Pair defaultAcceleration = new Pair(0,1200);
    static Boolean flapWing = true;

    static World gameWorld;
    Thread gameThread;
    static Timer gameTimer; //static - belongs to GameScreen, not instance of Timer
    public int gameTime; //referenced in other classes
    public static Boolean started = false;


    public GameScreen() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        gameWorld = new World(screenWidth, screenHeight);
        gameThread = new Thread(new GameScreen.Runner());
        gameThread.start();
    }

    class Runner implements Runnable {
        @Override
        public void run() {
            while(gameThread != null) {
                if(started) {
                gameWorld.updateObjects(1.0 / (double) FPS);
                    if(gameTimer==null) {
                        gameTimer = new Timer();
                    }
                    gameTime = gameTimer.getElapsedTime(); //keeps track of current time
                }
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
               // System.out.println("The game loop is running"); //
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        gameWorld.drawObjects(g);
        if(!started) {
            startMessage(g);
        }
    }

    public void paintBackground(Graphics g) {
        g.drawImage(loadResources.background, 0,0,screenWidth, screenHeight, null);
    }

    void startMessage(Graphics g) {
        g.setColor(java.awt.Color.MAGENTA);
        g.fillRect(((screenWidth / 2) - 90), screenHeight - 90, 150 + 4, 20);
        g.setColor(java.awt.Color.WHITE);
        g.drawRect(((screenWidth / 2) - 90), screenHeight - 90, 150 + 4, 20);
        g.setColor(java.awt.Color.WHITE);
        g.drawString("PRESS  ' S '  TO START", ((screenWidth / 2) - 80), screenHeight - 75);
    }

    static int peekHeight() { return screenHeight; }

    static int peekWidth() { return screenWidth; }



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
                case 'w' : gameWorld.player1.setAcceleration(defaultAcceleration); flapWing = !flapWing; gameWorld.player1.velocity.y = -1 * GameScreen.defaultYspeed; break;
                case 's' : started = true; gameWorld.player1.setAcceleration(defaultAcceleration); gameWorld.player1.velocity.x = 0;  Pipes.changePipeSpeed(Pipes.defaultPipeSpeed); break; //horizontal stop
                case 'a' : gameWorld.player1.setAcceleration(defaultAcceleration); gameWorld.player1.velocity.x = (-1.7 * GameScreen.defaultXspeed); Pipes.changePipeSpeed((int)(0.9*Pipes.defaultPipeSpeed)); break;
                case 'd' : gameWorld.player1.setAcceleration(defaultAcceleration); gameWorld.player1.velocity.x = GameScreen.defaultXspeed; Pipes.changePipeSpeed((int)(1.3*Pipes.defaultPipeSpeed)); break;
            }
        }

}
