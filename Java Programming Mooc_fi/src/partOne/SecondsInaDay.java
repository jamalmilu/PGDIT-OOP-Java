package partOne;

import java.util.Scanner;

public class SecondsInaDay {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("How many days would you like to convert to seconds?");
        int number = Integer.valueOf(input.nextLine());
        int calculation = number * 24 * 3600;
        System.out.println(calculation);

    }
}
