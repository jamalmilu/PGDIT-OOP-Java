// Please explain my code step by step:
package elementaryProgramming;

import java.util.Scanner;

public class AreaCalculator {
    public static void computeAndDisplayArea(){
        // Create a Scanner object
        Scanner input = new Scanner(System.in);

        // Prompt the user to enter a radius
        System.out.print("Enter a number for radius: ");
        double radius = input.nextDouble();

        // Compute area
        double area = radius * radius * 3.14159;

        // Display results
        System.out.println("--- Calculation Result ---");
        System.out.println("Radius enterened: " + radius);
        System.out.println("The calculated area is: " + area);
        System.out.println("--------------------------");

        // Close the Scanner
        input.close();
    }

}
