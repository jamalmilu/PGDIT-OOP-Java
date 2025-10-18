package exceptionHandling;

import java.util.Scanner;

public class Quotient {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Prompt the user to enter two integers
        for (int i = 1; i <= 5; i++) {
            System.out.print("Enter two integers: ");
            try {
                int number1 = input.nextInt();
                int number2 = input.nextInt();

                System.out.println(number1 + "/" + number2 + " is " + (number1 / number2));
            }
            catch (ArithmeticException arithmeticEx) {
                System.out.println("Please do not put 0 as the second input");
            }
        }

    }

}
