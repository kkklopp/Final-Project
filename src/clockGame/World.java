package clockGame;

import Timers.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class World {
    private static int height;
    private static int width;
    private ClockBackground[] backgrounds = new ClockBackground[4];
    BufferedImage[] backgroundImages = new BufferedImage[4];
    private GamePlayer player1;
    private Pipes pipe1;
    private Pipes pipe2;
    private Pipes pipe3;
    private static Pair pipe1default;
    private static Pair pipe2default;
    private static Pair pipe3default;
    private Hammer hammer;
    private static Pair hammerResetPosition = new Pair(-100, -100);
    private static Pair hammerResetVelocity = new Pair(0, 0);
    private CollisionDetection playerTopPipe1;
    private CollisionDetection playerBottomPipe1;
    private CollisionDetection playerTopPipe2;
    private CollisionDetection playerBottomPipe2;
    private CollisionDetection playerTopPipe3;
    private CollisionDetection playerBottomPipe3;
    private CollisionDetection playerHammer;
    private boolean takeDamage = true;
    private boolean drawDamage = false;
    private double damageTotal;
    private Timer hammerTimer;
    private Timer damageTimer;
    private Timer loseProgressTimer;
    private int prevHammerTime = -1;
    private int prevDamageTime = -1;
    private int prevloseProgressTime = -1;
    public boolean win;
    private double winCondition;
    private int wordWidth;
    private String winningMessage;
    private String scoreString;
    private int record;
    private int scoreBoardWidth;
    private boolean loseThree = false;
    private boolean loseTwo = false;
    private boolean loseOne = false;
    private String numHammersLost;
    DisplayGameScreen game;
    private static int hammerFrequency = 120; //one hammer created every hammerFrequency frames

    public World(int initWidth, int initHeight, int hammerGoal, int highScore, DisplayGameScreen initGame) {
        createBackground();
        game = initGame;
        damageTotal = 0;
        record = highScore;
        winCondition = hammerGoal;
        hammerTimer = new Timer();
        damageTimer = new Timer();
        loseProgressTimer = new Timer();
        width = initWidth;
        height = initHeight;
        player1 = new GamePlayer(game);
        hammer = new Hammer(-100, -100, 0); //start offscreen
        createPipes();
        collisionPipePlayer();
        collisionPlayerHammer();
    }

    private void createPipes() {
        pipe1 = new Pipes(GameScreen.loadResources.greenTopPipe, GameScreen.loadResources.greenBottomPipe);
        pipe1default = pipe1.peekPosition(); //defaults to most current pipe position when resetting pipes
        pipe2 = new Pipes((width / 3) + pipe1.width + pipe1.getPipeSpacing(), GameScreen.loadResources.yellowTopPipe, GameScreen.loadResources.yellowBottomPipe);
        pipe2default = pipe2.peekPosition();
        pipe3 = new Pipes((width / 3) + (2 * pipe2.width) + (2 * pipe2.getPipeSpacing()), GameScreen.loadResources.redTopPipe, GameScreen.loadResources.redBottomPipe);
        pipe3default = pipe3.peekPosition();
    }

    private void createBackground() {
        backgroundImages[0] = GameScreen.loadResources.background1;
        backgroundImages[1] = GameScreen.loadResources.background2;
        backgroundImages[2] = GameScreen.loadResources.background3;
        backgroundImages[3] = GameScreen.loadResources.background4;

        for (int i = 0; i < 4; i++) {
            backgrounds[i] = new ClockBackground((i * ClockBackground.peekDefaultWidth()), backgroundImages[i]);
        }
    }


    void drawObjects(Graphics g) {
        drawBackground(g);
        player1.draw(g);
        drawCrack(g);
        drawHammer(g);
        drawPipes(g);
        drawHammerScore(g);
        if (win) {
            drawIfWon(g);
        }
    }

    private void drawBackground(Graphics g) {
        for (int i = 0; i < 4; i++) {
            if (backgrounds[i].position.x < width) {
                g.drawImage(backgroundImages[i], (int) backgrounds[i].position.x, (int) backgrounds[i].position.y, (int) backgrounds[i].width, (int) backgrounds[i].height, null);
            }
        }
    }

    private void drawCrack(Graphics g) {
        if ((damageTotal / winCondition) >= (1.0 / 5.0) && (damageTotal / winCondition) < (2.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack1, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (2.0 / 5.0) && (damageTotal / winCondition) < (3.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack2, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (3.0 / 5.0) && (damageTotal / winCondition) < (4.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack3, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (4.0 / 5.0) && (damageTotal / winCondition) < (5.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack4, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (1.0)) {
            g.drawImage(GameScreen.loadResources.crack5, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
    }

    private void drawPipes(Graphics g) {
        pipe1.draw(g);
        pipe2.draw(g);
        pipe3.draw(g);
    }

    private void drawHammer(Graphics g) {
        hammer.draw(g);
        drawHammerDamage(g);
    }

    private void drawHammerDamage(Graphics g) {
        drawHammerExplosion(g);
        drawHammerPopUp(g);
    }

    private void drawHammerPopUp(Graphics g) {
        loseProgressTimer.frameInterval(100);
        int popUpPosition = (height / 2) - (int) (player1.height / 2);

        if ((loseOne || loseTwo || loseThree) && prevloseProgressTime == loseProgressTimer.getIntervalTime()) {
            g.drawImage(GameScreen.loadResources.loseHammer, (width / 2) + (int) (player1.width / 2), popUpPosition, (int) (hammer.width), (int) (hammer.height), null);
            if (loseOne) {
                numHammersLost = "" + 1;
            }
            if (loseTwo) {
                numHammersLost = "" + 2;
            }
            if (loseThree) {
                numHammersLost = "" + 3;
            }
            g.setFont(GameScreen.loadResources.MonospacedBold);
            g.setColor(Color.BLACK);
            g.drawString("-" + numHammersLost + " ", (5 + (width / 2) - (int) (1.5 * player1.width) + (int) (hammer.width) + 10), popUpPosition + (int) (hammer.height / 2));
            g.setColor(GameScreen.loadResources.lightRed);
            g.drawString("-" + numHammersLost + " ", ((width / 2) - (int) (1.5 * player1.width) + (int) (hammer.width) + 10), popUpPosition + (int) (hammer.height / 2));
        }

        if (prevloseProgressTime != loseProgressTimer.getIntervalTime()) { //stop drawing when it gets to a new second
            loseOne = false;
            loseTwo = false;
            loseThree = false;
        }
        prevloseProgressTime = loseProgressTimer.getIntervalTime();

    }

    private void drawHammerExplosion(Graphics g) {
        damageTimer.frameInterval(35);
        if (drawDamage && prevDamageTime == damageTimer.getIntervalTime()) {
            g.drawImage(GameScreen.loadResources.damage, (int) player1.position.x, (int) player1.position.y, (int) player1.width, (int) player1.height, null);
        }
        if (prevDamageTime != damageTimer.getIntervalTime()) { //stop drawing when it gets to a new second
            drawDamage = false;
        }
        prevDamageTime = damageTimer.getIntervalTime();
    }

    private void drawHammerScore(Graphics g) {
        drawScoreBoard(g);
        drawScoreBoardHammer(g);

        g.setColor(GameScreen.loadResources.lightBlue);
        g.setFont(GameScreen.loadResources.Monospaced);
        scoreString = ("high score : " + record);
        wordWidth = g.getFontMetrics().stringWidth(scoreString);
        g.drawString(scoreString, (scoreBoardWidth - wordWidth) / 2, 10 + (int) (2 * hammer.height / 3));
    }

    private void drawScoreBoardHammer(Graphics g) {
        for (int i = 0; i < damageTotal; i++) {
            g.drawImage(GameScreen.loadResources.collectHammer, 12 + (i * (int) (10 + (hammer.width / 3))), 20, (int) (hammer.width / 3), (int) (hammer.height / 3), null);
        }
        for (int i = 0; i < (winCondition - damageTotal); i++) {
            g.drawImage(GameScreen.loadResources.greyHammer, 12 + ((i + (int) damageTotal) * (int) (10 + (hammer.width / 3))), 20, (int) (hammer.width / 3), (int) (hammer.height / 3), null);
        }
    }

    private void drawScoreBoard(Graphics g) {
        g.setColor(Color.BLACK);
        if (winCondition <= 3) {
            scoreBoardWidth = (int) (8 * (hammer.width / 3));
        } else if (winCondition > 2) {
            scoreBoardWidth = 40 + (((int) winCondition - 1) * (int) (10 + (hammer.width / 3)));
        }
        g.fillRect(0, 10, scoreBoardWidth, 40 + (int) (hammer.height / 3));
        g.setColor(Color.WHITE);
        g.drawRect(0, 10, scoreBoardWidth, 40 + (int) (hammer.height / 3));
    }

    private void drawIfWon(Graphics g) {
        g.setFont(GameScreen.loadResources.MonospacedBold);
        g.setColor(GameScreen.loadResources.lightBlue);
        winningMessage = "Rise and Shine! Game Over!";
        wordWidth = g.getFontMetrics().stringWidth(winningMessage);
        g.drawString(winningMessage, (width / 2) - (wordWidth / 2), (height / 2) - 50);
    }

    void updateObjects(double time) {
        updateBackgrounds(time);
        updatePlayer(time);
        updatePipes(time);
        updateHammer(time);
        updatePipePlayerCollision();
        updatePlayerHammerCollision();
    }

    private void updateBackgrounds(double time) {
        for (int i = 0; i < 4; i++) {
            backgrounds[i].velocity.x = (0.12 * Pipes.getPipeSpeed());
            backgrounds[i].update(time);
        }
    }

    private void updatePlayer(double time) {
        player1.update(time);
    }

    private void updatePipes(double time) {
        if (game.gameInstance.ifStarted()) { //update velocity according to pipespeed variable
            pipe1.updatePipeSpeed();
            pipe2.updatePipeSpeed();
            pipe3.updatePipeSpeed();
        }
        pipe1.update(time); //update position according to velocity
        pipe2.update(time);
        pipe3.update(time);
        pipe1.updateRanPipePosition(); //update position to match random Y
        pipe2.updateRanPipePosition();
        pipe3.updateRanPipePosition();
    }

    private void updateHammer(double time) {
        hammerTimer.frameInterval(hammerFrequency);
        if (prevHammerTime != hammerTimer.getIntervalTime()) {
            takeDamage = true; ////after an interval of time, the hammer is able to take damage again
            reCreateHammer();
        }
        prevHammerTime = hammerTimer.getIntervalTime();
        hammer.velocity.x = Pipes.getPipeSpeed(); //update hammer velocity if pipespeed changes
        hammer.update(time);
    }

    private void reCreateHammer() {
        switch (ranPipeNum()) {
            case 1: //if 1, emerge from topPipe1
                hammer.position.x = pipe1.position.x + (pipe1.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe1.position.y + pipe1.height - hammer.height;
                hammer.velocity.y = Math.abs(hammer.getDefaultYVelocity());
                break;
            case 2: //If 2, emerge from topPipe2
                hammer.position.x = pipe2.position.x + (pipe2.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe2.position.y + pipe1.height - hammer.height;
                hammer.velocity.y = Math.abs(hammer.getDefaultYVelocity());
                break;
            case 3: //If 3, emerge from topPipe3
                hammer.position.x = pipe3.position.x + (pipe3.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe3.position.y + pipe1.height - hammer.height;
                hammer.velocity.y = Math.abs(hammer.getDefaultYVelocity());
                break;
            case 4://if 4, emerge from bottomPipe1
                hammer.position.x = pipe1.position.x + (pipe1.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe1.getBottomPosition().y + hammer.height;
                hammer.velocity.y = (-1 * (Math.abs(hammer.getDefaultYVelocity())));
                break;
            case 5://if 5, emerge from bottomPipe2
                hammer.position.x = pipe2.position.x + (pipe2.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe2.getBottomPosition().y + hammer.height;
                hammer.velocity.y = (-1 * (Math.abs(hammer.getDefaultYVelocity())));
                break;
            case 6://if 6, emerge from bottomPipe3
                hammer.position.x = pipe3.position.x + (pipe3.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe3.getBottomPosition().y + hammer.height;
                hammer.velocity.y = (-1 * (Math.abs(hammer.getDefaultYVelocity())));
                break;
        }
    }

    private void updatePipePlayerCollision() {
        playerTopPipe1.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.position.x, (int) pipe1.position.y, (int) (pipe1.position.x + pipe1.width), (int) (pipe1.position.y + pipe1.height));
        playerBottomPipe1.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.getBottomPosition().x, (int) pipe1.getBottomPosition().y, (int) (pipe1.getBottomPosition().x + pipe1.width), (int) (pipe1.getBottomPosition().y + pipe1.height));
        playerTopPipe2.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.position.x, (int) pipe2.position.y, (int) (pipe2.position.x + pipe2.width), (int) (pipe2.position.y + pipe2.height));
        playerBottomPipe2.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.getBottomPosition().x, (int) pipe2.getBottomPosition().y, (int) (pipe2.getBottomPosition().x + pipe2.width), (int) (pipe2.getBottomPosition().y + pipe2.height));
        playerTopPipe3.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.position.x, (int) pipe3.position.y, (int) (pipe3.position.x + pipe3.width), (int) (pipe3.position.y + pipe3.height));
        playerBottomPipe3.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.getBottomPosition().x, (int) pipe3.getBottomPosition().y, (int) (pipe3.getBottomPosition().x + pipe3.width), (int) (pipe3.getBottomPosition().y + pipe3.height));
        pipePlayerOverlap();
    }

    private void updatePlayerHammerCollision() {
        playerHammer.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) (hammer.position.x), (int) (hammer.position.y), (int) (hammer.position.x + hammer.width), (int) (hammer.position.y + hammer.height));
        playerHammerOverlap();
    }


    private void pipePlayerOverlap() {
        if (playerTopPipe1.checkOverlap() || playerTopPipe2.checkOverlap() || playerTopPipe3.checkOverlap() || playerBottomPipe1.checkOverlap() || playerBottomPipe2.checkOverlap() || playerBottomPipe3.checkOverlap()) { //if true, reset
            resetHammer();
            resetPipes();
            player1.resetPlayer();
            drawDamage = false;
        }

        if ((playerTopPipe1.checkOverlap() || playerBottomPipe1.checkOverlap()) && (damageTotal > 0)) { //if true, reset
            damageTotal -= 1;
            loseOne = true;
        }
        if ((playerTopPipe2.checkOverlap() || playerBottomPipe2.checkOverlap()) && (damageTotal > 0)) { //if true, reset
            damageTotal -= 2;
            loseTwo = true;
        }
        if ((playerTopPipe3.checkOverlap() || playerBottomPipe3.checkOverlap()) && (damageTotal > 0)) { //if true, reset
            damageTotal -= 3;
            loseThree = true;
        }
        if (damageTotal < 0) {
            damageTotal = 0;
        }
    }

    private void playerHammerOverlap() {
        if (playerHammer.checkOverlap() && takeDamage) { //if true, damage+=1 //explode hammer, add crack to screen
            takeDamage = false;
            drawDamage = true;
            damageTotal++;
            resetHammer();
            damageTimer.clearIntervalTimer(); //reset drawing timer for hammer to 0 so the drawing interval is accurate
        }
    }

    private void collisionPipePlayer() {
        playerTopPipe1 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.position.x, (int) pipe1.position.y, (int) (pipe1.position.x + pipe1.width), (int) (pipe1.position.y + pipe1.height));
        playerBottomPipe1 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.getBottomPosition().x, (int) pipe1.getBottomPosition().y, (int) (pipe1.getBottomPosition().x + pipe1.width), (int) (pipe1.getBottomPosition().y + pipe1.height));

        playerTopPipe2 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.position.x, (int) pipe2.position.y, (int) (pipe2.position.x + pipe2.width), (int) (pipe2.position.y + pipe2.height));
        playerBottomPipe2 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.getBottomPosition().x, (int) pipe2.getBottomPosition().y, (int) (pipe2.getBottomPosition().x + pipe2.width), (int) (pipe2.getBottomPosition().y + pipe2.height));

        playerTopPipe3 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.position.x, (int) pipe3.position.y, (int) (pipe3.position.x + pipe3.width), (int) (pipe3.position.y + pipe3.height));
        playerBottomPipe3 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.getBottomPosition().x, (int) pipe3.getBottomPosition().y, (int) (pipe3.getBottomPosition().x + pipe3.width), (int) (pipe3.getBottomPosition().y + pipe3.height));
    }

    private void collisionPlayerHammer() {
        playerHammer = new CollisionDetection((int) (player1.width / 2), (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) (hammer.position.x), (int) (hammer.position.y), (int) (hammer.position.x + hammer.width), (int) (hammer.position.y + hammer.height));
    }

    private void resetPipePosition() {
        pipe1.setPosition(pipe1default);
        pipe2.setPosition(pipe2default);
        pipe3.setPosition(pipe3default);
    }

    private static void resetPipeSpeeds() {
        Pipes.changePipeSpeed(Pipes.getDefaultPipeSpeed());
    }

    void resetPipes() {
        resetPipeSpeeds();
        resetPipePosition();
    }

    void resetHammer() {
        hammer.setPosition(hammerResetPosition);
        hammer.setVelocity(hammerResetVelocity);
    }

    private int ranPipeNum() {
        int pipeNum = (int) (Math.random() * 6 + 1);
        return pipeNum;
    }

    public void setPlayerXVelocity(double x) {
        player1.velocity.x = x;
    }

    public void setPlayerYVelocity(double y) {
        player1.velocity.y = y;
    }

    public void setPlayerAcceleration(Pair xyz) {
        player1.acceleration = xyz;
    }

    public void setDamageTotal(int damageNum) {
        damageTotal = damageNum;
    }

    public boolean checkIfWon() {
        if (damageTotal == winCondition) {
            win = true;
        }
        return win;
    }

}


