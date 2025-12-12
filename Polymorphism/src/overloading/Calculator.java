package overloading;

public class Calculator {
    // Method 1: Add two integers
    public int add(int a, int b){
        return a + b;
    }

    // Method 1: Add three integers(Different number of parameter)
    public int add(int a, int b, int c){
        return a + b + c;
    }

    // Method 3: Add two doubles (Different parameter types)
    public double add(double a, double b){
        return a + b;
    }

    // Method 4: Add integer and double(Different parameter order)
    public double add(int a, double b){
        return a + b;
    }

    // Method 5: Add double and integer(Different parameter order)
    public double add(double a, int b){
        return a + b;
    }
}
