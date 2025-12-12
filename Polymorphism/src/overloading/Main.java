package overloading;

public class Main {
    public static void main(String[] args){
        Calculator calc = new Calculator();

        System.out.println(calc.add(5,10));
        System.out.println(calc.add(5, 10, 15));
        System.out.println(calc.add(5.5, 10.5));
        System.out.println(calc.add(5,10.5));
        System.out.println(calc.add(5.5,10));
    }
}
