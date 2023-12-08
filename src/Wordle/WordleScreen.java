package Wordle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WordleScreen extends JPanel implements KeyListener, MouseListener {
        public static final int WIDTH = 900;
        public static final int HEIGHT = 700;
        public static final int FPS = 60;


        World world;
        String new_word;
        //String userWord;
        int y = 0;
        int numGuesses = 0;
        Word word;
        int onWhichWord;
        boolean win = false;
        boolean lose = false;

        char[][] userWordsArray = new char[8][5];
        String[][] colorsArray = new String[8][5];

        char[] keyboardFirstRow = {'Q','W','E','R','T','Y','U','I','O','P'};
        VirtualKeyboardKey[] keyboardRow1 = new VirtualKeyboardKey[10];
        String[] keyboardFirstRowColor = new String[10];

        char[] keyboardSecondRow = {'A','S','D','F','G','H','J','K','L'};
        VirtualKeyboardKey[] keyboardRow2 = new VirtualKeyboardKey[9];
        String[] keyboardSecondRowColor = new String[9];

        char[] keyboardThirdRow = {'Z','X','C','V','B','N','M'};
        VirtualKeyboardKey[] keyboardRow3 = new VirtualKeyboardKey[7];
        String[] keyboardThirdRowColor = new String[7];

        int coins = 0;

        //Char[] letterArray = new Char[5];





        public WordleScreen(){
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));


            addKeyListener(this);
            addMouseListener(this);
            world = new World(WIDTH, HEIGHT);

            Thread mainThread = new Thread(new Runner());
            mainThread.start();

            initialize();

            //userWord = world.takeInput();

        }

        public void initialize(){

            win = false;
            lose = false;
            new_word = world.readFile();
            onWhichWord = 0;

            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 5; j++){

                    colorsArray[i][j] = "white";
                    userWordsArray[i][j] = ' ';
                }

            }

            for (int i = 0; i < 10; i ++){
                keyboardFirstRowColor[i] = "white";
                keyboardRow1[i] = new VirtualKeyboardKey();
                if (i < 9){
                    keyboardSecondRowColor[i] = "white";
                    keyboardRow2[i] = new VirtualKeyboardKey();
                }
                if (i < 7){
                    keyboardThirdRowColor[i] = "white";
                    keyboardRow3[i] = new VirtualKeyboardKey();
                }
            }

        }


        class Runner implements Runnable{
            public Runner()
            {

            }
            public void run()
            {
                while(true){

                    //System.out.println("Hello");

                    //world.updateWorld(1.0 / (double)FPS);
                    repaint();
                    try{
                        Thread.sleep(1000/FPS);
                    }
                    catch(InterruptedException e){}
                }
            }

        }

        public void mousePressed(MouseEvent e)
        {

            char c = ' ';
            // show the point where the user pressed the mouse
            for (int i = 0; i < 10; i++){
                if (((keyboardRow1[i].startX <= e.getX()))&& (keyboardRow1[i].startX + keyboardRow1[i].width >= e.getX()) &&
                        ((keyboardRow1[i].startY <= e.getY()))&& (keyboardRow1[i].startY + keyboardRow1[i].height >= e.getY())){
                    c = keyboardRow1[i].letter;
                    break;
                }

                if (i < 9){
                    if (((keyboardRow2[i].startX <= e.getX()))&& (keyboardRow2[i].startX + keyboardRow2[i].width >= e.getX()) &&
                            ((keyboardRow2[i].startY <= e.getY()))&& (keyboardRow2[i].startY + keyboardRow2[i].height >= e.getY())){
                        c = keyboardRow2[i].letter;
                        break;
                    }
                }

                if (i < 7){
                    if (((keyboardRow3[i].startX <= e.getX()))&& (keyboardRow3[i].startX + keyboardRow3[i].width >= e.getX()) &&
                            ((keyboardRow3[i].startY <= e.getY()))&& (keyboardRow3[i].startY + keyboardRow3[i].height >= e.getY())){
                        c = keyboardRow3[i].letter;
                        break;
                    }
                }
            }

            if (c != ' '){
                for (int i = 0; i < 5; i++){
                    if (userWordsArray[onWhichWord][i] == ' '){
                        userWordsArray[onWhichWord][i] = c;
                        break;
                    }

                }
            }


            //System.out.println("mouse pressed at point:" + e.getX() + " " + e.getY());
        }

        // this function is invoked when the mouse is released
        public void mouseReleased(MouseEvent e)
        {

            // show the point where the user released the mouse click

        }

        // this function is invoked when the mouse exits the component
        public void mouseExited(MouseEvent e)
        {

            // show the point through which the mouse exited the frame

        }

        // this function is invoked when the mouse enters the component
        public void mouseEntered(MouseEvent e)
        {

            //System.out.println("bye");
            // show the point through which the mouse entered the frame

        }

        // this function is invoked when the mouse is pressed or released
        public void mouseClicked(MouseEvent e)
        {


        }

        public void keyPressed(KeyEvent e) {

            char c = e.getKeyChar();

            //System.out.println("You pressed down: " + c);
        }

        public void keyReleased(KeyEvent e) {
            char c=e.getKeyChar();

            //System.out.println("You Released: " + c);
        }

        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            System.out.println((int)c);

            if ((lose == true || win == true) && ((int)c == 32)){
                initialize();
            }

            if ((int)c == 27 && coins >=10){ //pressing esc key for hint
                for (int i = 0; i < 5; i++){
                    boolean green = false;
                    for (int j = 0; j < onWhichWord+1; j++){
                        if (colorsArray[j][i] == "green"){
                            green = true;
                        }
                    }
                    if ((onWhichWord == 0  && colorsArray[onWhichWord][i] != "green") || (green == false)){

                        userWordsArray[onWhichWord][i] = new_word.toUpperCase().charAt(i);
                        colorsArray[onWhichWord][i] = "green";
                        coins -= 10;
                        break;
                    }
                    //if ( userWordsArray[onWhichWord-1][i]){}
                    //new_word.charAt(i)
                }
            }

            else if ((int)c == 10){
                String userWord = new String(userWordsArray[onWhichWord]);
                if (!world.doesWordExist(userWord)){
                    for (int i = 0; i < 5; i++){
                        userWordsArray[onWhichWord][i] = ' ';
                    }

                }
                else{

                    colorsArray[onWhichWord] = world.colorWord(new_word, userWord.toLowerCase());

                    for (int i = 0; i < 5; i++){
                        for (int j = 0; j < 10; j++){
                            if (userWordsArray[onWhichWord][i] == keyboardFirstRow[j]){
                                if ((keyboardFirstRowColor[j] == "white") || (keyboardFirstRowColor[j] == "grey" && colorsArray[onWhichWord][i] == "yellow") || (colorsArray[onWhichWord][i] == "green")){
                                    keyboardFirstRowColor[j] = colorsArray[onWhichWord][i];
                                }

                            }
                            if (j<9){
                                if (userWordsArray[onWhichWord][i] == keyboardSecondRow[j]){
                                    if ((keyboardSecondRowColor[j] == "white") || (keyboardSecondRowColor[j] == "grey" && colorsArray[onWhichWord][i] == "yellow") || (colorsArray[onWhichWord][i] == "green")){
                                        keyboardSecondRowColor[j] = colorsArray[onWhichWord][i];
                                    }
                                }
                            }
                            if (j<7){
                                if (userWordsArray[onWhichWord][i] == keyboardThirdRow[j]){
                                    if ((keyboardThirdRowColor[j] == "white") || (keyboardThirdRowColor[j] == "grey" && colorsArray[onWhichWord][i] == "yellow") || (colorsArray[onWhichWord][i] == "green")){
                                        keyboardThirdRowColor[j] = colorsArray[onWhichWord][i];
                                    }
                                }
                            }
                        }
                    }



                    onWhichWord++;
                    if (new_word.equals(userWord.toLowerCase())){
                        win = true;
                        for (int i =0; i < 8; i ++){
                            if (numGuesses == i){
                                coins += (8 - onWhichWord)*5;
                            }
                        }
                        //stop alarm here
                    }
                    else if (onWhichWord>7){
                        lose = true;
                        onWhichWord --;
                    }
                }

            }



            else if ((int)c == 8){
                boolean done = false;
                if (userWordsArray[onWhichWord][0] != ' '){
                    for (int i = 0; i < 5; i ++){
                        if (userWordsArray[onWhichWord][i] == ' '){
                            userWordsArray[onWhichWord][i-1] = ' ';
                            done = true;
                        }
                    }
                    if (done == false){
                        userWordsArray[onWhichWord][4] = ' ';
                    }
                }

            }

            else {
                c = Character.toString(c).toUpperCase().charAt(0);
                for (int i = 0; i < 5; i++){
                    if (userWordsArray[onWhichWord][i] == ' '){
                        userWordsArray[onWhichWord][i] = c;
                        break;
                    }

                }
            }



            //System.out.println("You Typed: " + c);
        }

        public void addNotify() {
            super.addNotify();
            requestFocus();
        }

        //String random_word = Wordle.World.getWord("five_letter_words.txt");



        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            //g.drawString("hello", 200, 200);



            //System.out.println(random_word);


            super.paintComponent(g);



            g.setColor(Color.BLACK);
            //System.out.println("");
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setFont(new Font("Roboto", Font.PLAIN, 25));

            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 5; j++){
                    if (colorsArray[i][j] == "white"){
                        g.setColor(Color.WHITE);
                    }
                    else if (colorsArray[i][j] == "grey"){
                        g.setColor(Color.GRAY);
                    }
                    else if (colorsArray[i][j] == "green"){
                        g.setColor(Color.GREEN);
                    }
                    else if (colorsArray[i][j] == "yellow"){
                        g.setColor(Color.YELLOW);
                    }
                    g.fillRect(350 + 60*j, 75 + 40*i, 50, 35);
                    g.setColor(Color.BLACK);
                    g.drawString(Character.toString(userWordsArray[i][j]), 365 + 60*j , 100 + 40*i);
                }
            }

            g.setFont(new Font("Roboto", Font.PLAIN, 28));

            //printing keyboard
            for (int i = 0; i < 10; i++){

                //first row
                if (keyboardFirstRowColor[i] == "white"){
                    g.setColor(Color.WHITE);
                }
                else if (keyboardFirstRowColor[i] == "grey"){
                    g.setColor(Color.GRAY);
                }
                else if (keyboardFirstRowColor[i] == "green"){
                    g.setColor(Color.GREEN);
                }
                else if (keyboardFirstRowColor[i] == "yellow"){
                    g.setColor(Color.YELLOW);
                }


                g.fillRect(180 + 65*i, 410, 50, 50);
                g.setColor(Color.BLACK);
                g.drawString(Character.toString(keyboardFirstRow[i]), 195 + 65*i , 440);
                keyboardRow1[i].startX = (180 + 65*i);
                keyboardRow1[i].startY = 410;
                keyboardRow1[i].letter = keyboardFirstRow[i];

                //second row
                if (i<9){

                    if (keyboardSecondRowColor[i] == "white"){
                        g.setColor(Color.WHITE);
                    }
                    else if (keyboardSecondRowColor[i] == "grey"){
                        g.setColor(Color.GRAY);
                    }
                    else if (keyboardSecondRowColor[i] == "green"){
                        g.setColor(Color.GREEN);
                    }
                    else if (keyboardSecondRowColor[i] == "yellow"){
                        g.setColor(Color.YELLOW);
                    }

                    g.fillRect(208 + 65*i, 475, 50, 50);
                    g.setColor(Color.BLACK);
                    g.drawString(Character.toString(keyboardSecondRow[i]), 223 + 65*i , 505);

                    keyboardRow2[i].startX = (208 + 65*i);
                    keyboardRow2[i].startY = 475;
                    keyboardRow2[i].letter = keyboardSecondRow[i];

                }

                //third row
                if (i<7){


                    if (keyboardThirdRowColor[i] == "white"){
                        g.setColor(Color.WHITE);
                    }
                    else if (keyboardThirdRowColor[i] == "grey"){
                        g.setColor(Color.GRAY);
                    }
                    else if (keyboardThirdRowColor[i] == "green"){
                        g.setColor(Color.GREEN);
                    }
                    else if (keyboardThirdRowColor[i] == "yellow"){
                        g.setColor(Color.YELLOW);
                    }

                    g.fillRect(273 + 65*i, 540, 50, 50);
                    g.setColor(Color.BLACK);
                    g.drawString(Character.toString(keyboardThirdRow[i]), 288 + 65*i , 570);

                    keyboardRow3[i].startX = (273 + 65*i);
                    keyboardRow3[i].startY = 540;
                    keyboardRow3[i].letter = keyboardThirdRow[i];
                }
            }


            drawWinLose(g);

            g.setFont(new Font("TimesRoman", Font.BOLD, 25));
            g.setColor(Color.WHITE);
            g.drawString("You have " + Integer.toString(coins) + " coins!", 380, 30);
            g.drawString("In 10 coins you can ask for a letter hint by pressing esc", 180,60);


        }

        void drawWinLose(Graphics g) {
            if (win){
                g.setFont(new Font("TimesRoman", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Congratulations! You WON!!! Your alarm is stopped", 200, 635);
                g.drawString("You can press space if you would like to play again", 200, 675);


            }

            if (lose){
                g.setFont(new Font("TimesRoman", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Sorry, you lost :(, the word was " + new_word, 200, 635);
                g.drawString("You have to press space to play again", 200, 675);

            }
        }

        public boolean ifWordleComplete() {
            return win;
        }
    }

