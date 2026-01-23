package CallSuperclassConstructor;

public class Languages {
    // constructor of the superclass
    Languages(int version1, int version2){
        if (version1 > version2){
            System.out.println("The latest version is: " + version1);
        }
        else {
            System.out.println("The latest version is: " + version2);
        }
    }
}
// child class
class Main extends Languages{
    // constructor of the child class
    Main(){
        // calling the constructor of super class
        super(11,8);
    }

    // main method
    public static void main(String[] args) {

        // call the first constructor
        Main obj = new Main();
    }
}