package MissingSuperClass;

public class Main {
    public static void main(String[] args){
        Parent Jamal = new Parent("Jamal");
        Child Rayanna = new Child("Rayanna",8);

        System.out.println("---Jamal (Parent) Info---");
        Jamal.display();

        System.out.println("\n--- Rayanna (Child) Info ---");
        Rayanna.display();
    }
}
