package MathAttempt;
import javax.swing.JPanel;

public class CalculateMath {


    public CalculateMath() { // pass in the number of different arguments you want when calling the math class(like 1+1 is 2 arguments, while 1+1+1 is 3)
    }

    //gives the sum of all the random nums in the array
    public static int add(int [] numArray){
        int total = 0;
        for (int i = 0; i< numArray.length; i++){
            total+= numArray[i];
        }
        return total;
    }

    //does 1st num - 2nd num - 3rd num - 4th num and so on..
    public static int subtract(int [] numArray){
        int difference = numArray[0];
        for (int i = 1; i< numArray.length; i++){
            difference-= numArray[i];
        }
        return difference;

    }

    //gives the product of all the random nums in the array
    public static int multiply(int [] numArray){
        int product = 1;
        for (int i = 0; i< numArray.length; i++){
            product*= numArray[i];
        }
        return product;
    }
}
