package labExam1;

import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {
    private ArrayList<String> history = new ArrayList<>();
    private Scanner input = new Scanner(System.in);

    // 1. Function to print odd integers from range a to b
    public void productCalculate(int a, int b){
        System.out.println("Odd integers from " + a + " to " + b + ":");
        for (int i = a; i <= b; i++){
            if(i % 2 != 0){
                System.out.print(i + " ");  // Changed println to print
            }
        }
        // New line after printing all odds
        System.out.println();

        // Add to history
        history.add("Printed odd numbers from " + a + " to " + b);  // Added spaces
    }

    // 2. Method overloading for sumCalculate with 2 parameters
    public int sumCalculate(int num1, int num2) {
        int result = num1 + num2;
        history.add(num1 + " + " + num2 + " = " + result);
        return result;
    }

    // 2. Method overloading for sumCalculate with 3 parameters
    public int sumCalculate(int num1, int num2, int num3) {
        int result = num1 + num2 + num3;
        history.add(num1 + " + " + num2 + " + " + num3 + " = " + result);
        return result;
    }

    // 2. Method overloading for sumCalculate with 4 parameters
    public int sumCalculate(int num1, int num2, int num3, int num4) {
        int result = num1 + num2 + num3 + num4;
        history.add(num1 + " + " + num2 + " + " + num3 + " + " + num4 + " = " + result);
        return result;
    }

    // 3. Function to display calculation history
    public void calculationHistory() {
        System.out.print("Do you want to see calculation history? (yes/no): ");
        String choice = input.nextLine().toLowerCase();

        if (choice.equals("yes") || choice.equals("y")) {
            if (history.isEmpty()) {
                System.out.println("No calculations in history yet.");
            } else {
                System.out.println("\n=== Calculation History ===");
                for (int i = 0; i < history.size(); i++) {
                    System.out.println((i + 1) + ". " + history.get(i));
                }
                System.out.println("===========================\n");
            }
        }

    }
}