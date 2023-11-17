import java.util.random;
import java.lang;

public class Math{
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

}