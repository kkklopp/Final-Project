package Wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

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

class VirtualKeyboardKey{
    int startX;
    int width = 55;
    int startY;
    int height = 55;
    char letter;


    public VirtualKeyboardKey(){

    }
}