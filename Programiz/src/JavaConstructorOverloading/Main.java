package JavaConstructorOverloading;

public class Main {
    public static void main(String[] args) {
        // call constructor with no parameter
        Constructor obj1 = new Constructor();

        // call constructor with a single parameter
        Constructor obj2 = new Constructor("Python");

        obj1.getName();
        obj2.getName();
    }
}
