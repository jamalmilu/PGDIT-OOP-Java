package labExam1;

import java.util.Scanner;  // Added import

public class Main {
    // Main method to test the calculator
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner mainScanner = new Scanner(System.in);

        System.out.println("=== Java Calculator ===");

        // Test productCalculate
        System.out.println("\nTesting productCalculate:");
        calculator.productCalculate(1, 10);

        // Test sumCalculate with different numbers of parameters
        System.out.println("\nTesting sumCalculate with 2 numbers:");
        int result2 = calculator.sumCalculate(5, 10);
        System.out.println("5 + 10 = " + result2);

        System.out.println("\nTesting sumCalculate with 3 numbers:");
        int result3 = calculator.sumCalculate(5, 10, 15);
        System.out.println("5 + 10 + 15 = " + result3);

        System.out.println("\nTesting sumCalculate with 4 numbers:");
        int result4 = calculator.sumCalculate(5, 10, 15, 20);
        System.out.println("5 + 10 + 15 + 20 = " + result4);

        // Test calculationHistory
        calculator.calculationHistory();

        mainScanner.close();
    }
}
