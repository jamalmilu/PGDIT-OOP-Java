package ConstructorExample;

public class User {
    // Private fields: hidden from the outside world
    private String username;
    private int age;

    // Public Constructor: used to create the object and set initial values
    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // Public Getter: allows reader the private field
    public String getUsername() {
        return username;
    }
    public int getAge(){
        return age;
    }
    // Public Setter: allows changing the private field safely
    public void setAge(int age) {
        if (age > 0) { // You can add validation here!
            this.age = age;
        }

    }
}
