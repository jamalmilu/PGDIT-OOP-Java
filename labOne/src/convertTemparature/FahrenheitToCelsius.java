package convertTemparature;

import java.util.Scanner;

public class FahrenheitToCelsius {
    public static void convertFahrenheit(){
        // Create a Scanner object
        Scanner input = new Scanner(System.in);
        // Prompt the user input
        System.out.print("Enter a degree in Fahrenheit: ");

        // Read the Fahrenheit
        double fahrenheit = input.nextDouble();

        // Convert Fahrenheit to Celsius
        double celsius = (5.0/9) * (fahrenheit - 32);
        System.out.println("Fahrenheit "+ fahrenheit + " is " + celsius + " in Celsius.");

        // Close the Scanner
        input.close();
    }
}
