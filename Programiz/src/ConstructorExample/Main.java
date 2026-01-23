package ConstructorExample;

public class Main {
    public static void main(String[] args) {
        // 1. Create the object using the constructor
        User myUser = new User("Jamal", 51);

        // 2. Access data via the Getter(myUser.username would fail)
        System.out.println("Name: " + myUser.getUsername());
        System.out.println("Age: " + myUser.getAge());

        // 3. Modify data via the setter
        myUser.setAge(50);
        System.out.println("Corrected Age: " + myUser.getAge());
    }
}
