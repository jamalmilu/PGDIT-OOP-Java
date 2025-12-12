package MissingSuperClass;

public class Parent {
    String name;
    // A parameterized constructor is defined
    Parent(String name){
        this.name = name;
    }
    // Add a method to display the name
    public void display(){
        System.out.println("Parent Name: "+this.name);
    }
}

// File: Child
class Child extends Parent{
    int age;

    Child(String name, int age){
        super(name);
        this.age = age;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Child Age: "+ this.age);
    }
}
