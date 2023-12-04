import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.lang.Math;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;
import java.util.Scanner;
import java.util.Set;
import java.io.File; 
import java.io.FileNotFoundException;
import java.util.Random;  
import java.util.Scanner;
import java.util.*;
import java.awt.Font;


public class Wordle_copy{
    public static void main(String[] args){
        JFrame frame = new JFrame("Wordle!!!");
        
        // JLabel label = new JLabel(icon);
        // frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyboardObjects mainInstance = new KeyboardObjects();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
        //frame.repaint();

    
    }


}

class World{

    int height;
    int width;
    String wordArray[] = new String[5757];
    int x = 0;
    Scanner myObj = new Scanner(System.in);
    HashSet<String> set = new HashSet<>(); 
    //String colorArray[] = new String[5];

    public World(int initWidth, int initHeight){
        width = initWidth;
        height = initHeight;
        
        }

    public void update(World w, double time){

        //System.out.println(this.velocity.x);
        System.out.println(time);
        //System.out.println(this.position.x);
        //position = position.add(velocity.times(time));


    }

    // public String takeInput(){
    //     System.out.print("Enter your guess: ");
    //     String userWord;
    //     userWord = myObj.nextLine();
        
    //     while (doesWordExist(userWord) == false){
    //         System.out.print("Please enter a valid 5-letter word: ");
    //         userWord = myObj.nextLine();
    //     }
        
    //     System.out.println(userWord);
    //     return userWord;
    // }


    public boolean doesWordExist(String userWord){
        
        return set.contains(userWord.toLowerCase());
         
    }

    public String[] colorWord(String word, String userWord){

        String[] colorArray = new String[5];
        
        char[] wordArray = word.toCharArray();
        char[] userWordArray = userWord.toCharArray();

        for (int i = 0; i<5; i++){
            colorArray[i] = "grey";

            if (wordArray[i] == userWordArray[i]){
                colorArray[i] = "green";
                wordArray[i] = '#';
            }
            
        }

        for (int i = 0; i<5; i++){
            for (int j = 0; j<5; j++){
                if (colorArray[i] == "green"){
                    break;
                }
                if (userWordArray[i] == wordArray[j]){
                    colorArray[i] = "yellow";
                    wordArray[j] = '#';
                    break;
                }
            }
        
        }

        return colorArray;
    }

    public String readFile(){
        try {
            File myObj = new File("five_letter_words.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                
                String data = myReader.nextLine();
                //System.out.println(data);
                set.add(data);
                //wordArray[x] = data;
                //System.out.println(x);
                x++;
            }
            myReader.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    Random random = new Random();  
    String[] wordArray = set.toArray(new String[set.size()]);
    String randomElement = wordArray[random.nextInt(wordArray.length)];  
    System.out.println(randomElement);

    return randomElement;

    }
}

class Word{

    String[] colorArray;
    String userWord;
    World world;

    public Word(){
        colorArray = new String[5];
    }


}

class KeyboardObjects extends JPanel implements KeyListener{
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
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
    //Char[] letterArray = new Char[5];
    



    public KeyboardObjects(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        
        addKeyListener(this);
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

        else if ((int)c == 10){
            String userWord = new String(userWordsArray[onWhichWord]);
            if (!world.doesWordExist(userWord)){
                for (int i = 0; i < 5; i++){
                    userWordsArray[onWhichWord][i] = ' ';
                }
                
            }
            else{
                colorsArray[onWhichWord] = world.colorWord(new_word, userWord.toLowerCase());
                onWhichWord++;
                if (new_word.equals(userWord.toLowerCase())){
                    win = true;
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
        
        
            if (c == 'r'){
            
               
            }
            else if (c == 'f'){
                
            }
            else if (c == 'v'){
                
         
            }
            else if (c == 'u'){
                
                
            }
            else if (c == 'j'){
                
            }
            else if (c == 'n'){
                
            }

            
        //System.out.println("You Typed: " + c);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

   //String random_word = World.getWord("five_letter_words.txt");
    

    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        //g.drawString("hello", 200, 200);

        
        
        //System.out.println(random_word);
        
        
        super.paintComponent(g);  

        

        g.setColor(Color.BLACK);
        //System.out.println("");
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));

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
                g.fillRect(300 + 100*j, 100 + 75*i, 90, 70);
                g.setColor(Color.BLACK);
                g.drawString(Character.toString(userWordsArray[i][j]), 330 + 100*j , 150 + 75*i);
            }
        }
        
        if (win){
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString("Congratulations! You WON!!! Your alarm is stopped", 150, 60);
            g.drawString("You can press space if you would like to play again", 200, 740);

            
        }

        if (lose){
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString("Sorry, you lost :(, the word was " + new_word, 300, 60);
            g.drawString("You have to press space to play again", 300, 740);
            
        }

        
        // y += 75;
        
        

        // if ((numGuesses < 6) && !(userWord.toLowerCase().equals(new_word)) ){
        //     word = new Word();
        //     userWordsArray[numGuesses] = word;
        // }
        
        // numGuesses ++;


        

     

        // if (userWord.toLowerCase().equals(new_word)){
            
        //     g.setFont(new Font("TimesRoman", Font.BOLD, 30));
        //     g.setColor(Color.WHITE);
        //     g.drawString("Congratulations! You WON!!!", 300, 700);

        //     //stop alarm here 

        // }

        // else if (numGuesses > 6){

        //     g.setFont(new Font("TimesRoman", Font.BOLD, 30));
        //     g.setColor(Color.WHITE);
        //     g.drawString("Sorry, you lost :(, the word was " + new_word, 300, 700);

        // }
        // else{
        //     System.out.println("Check takeInput");
        //     userWord = world.takeInput();
        //     word.userWord = userWord.toUpperCase();
        //     System.out.println(numGuesses);
      
        //     word.colorArray = world.colorWord(new_word, userWord);
            
           

        //     userWord = userWord.toUpperCase();
            

        //     for (int i = 0; i < 5; i ++){
        //         if (word.colorArray[i] == "green"){
        //             g.setColor(Color.GREEN);
        //         }
        //         else if (word.colorArray[i] == "yellow"){
        //             g.setColor(Color.YELLOW);
        //         }
        //         else{
        //             g.setColor(Color.GRAY);
        //         }
                
        //         g.drawString(Character.toString(userWord.charAt(i)), 400 + 50*i, 150 + y);
        //     }

        // }

        // int x = 0;
        // while (x < 6 && userWordsArray[x] != null){
            
            
        //     for (int i = 0; i < 5; i ++){
        //         if (userWordsArray[x].colorArray[i] == "green"){
        //             g.setColor(Color.GREEN);
        //         }
        //         else if (userWordsArray[x].colorArray[i] == "yellow"){
        //             g.setColor(Color.YELLOW);
        //         }
        //         else{
        //             g.setColor(Color.GRAY);
        //         }
                
        //         g.drawString(Character.toString(userWordsArray[x].userWord.charAt(i)), 400 + 50*i, 225 + x*75);
        //     }
        //     x++;
        // }

    }    
}

