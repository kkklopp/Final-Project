import java.util.random;

import javax.swing.JPanel;

import java.lang;

public class Math extends JPanel{

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;
    public static final int FPS = 60;
    String operation = "";
   
    //array of random numbers
    int[] randomNumsArray;
    
 
    public Math(int n){ // pass in the number of diffenent arguments you want when calling the math class(like 1+1 is 2 arguments, while 1+1+1 is 3) 
        randomNumsArray = new int[n];
        generateRandom();
        
    }

    //store the random numbers in an array
    public void generateRandom(){
        for (int i = 0; i< randomNumsArray.length; i++){
            randomNumsArray[i] = (int) Math.random()*100;
        }

    }

    //gives the sum of all the random nums in the array
    public int add(){
        int total = 0;
        for (int i = 0; i< randomNumsArray.length; i++){
            total+= randomNumsArray[i];
        }
        return total;
    }

    //does 1st num - 2nd num - 3rd num - 4th num and so on..
    public int subtract(){
        int difference = randomNumsArray[0];
        for (int i = 1; i< randomNumsArray.length; i++){
            difference-= randomNumsArray[i];
        }
        return difference;

    }

    //gives the product of all the random nums in the array
    public int multiply(){
        int product = 1;
        for (int i = 0; i< randomNumsArray.length; i++){
            product+= randomNumsArray[i];
        }
        return product;

    }

    public String take_input(){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter answer");

        String answer = myObj.nextLine();
        return answer;
    }

    public boolean check_answer(int answer){
        
        if (answer == this.product()){
            return true;
        }
        return false;
    }


    @Override

    public void paintComponent(Graphics g) {
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 50);
        g.setColor(Color.ORANGE);
        g.setFont(font);
        g.drawString(Integer.toString(randomNumsArray[0]), 230, 350);
        g.drawString("X", 490, 350);
        g.drawString(Integer.toString(randomNumsArray[1]), 750, 350);
        String answer = take_input();
        System.out.println(check_answer((int)answer));

    }

}
