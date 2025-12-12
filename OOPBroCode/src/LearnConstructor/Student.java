package LearnConstructor;

public class Student {
    // instance variable
    String name;
    int age;
    double gpa;
    boolean isEnrolled;

    // Constructor for a Student class
    Student(String name, int age, double gpa){
        this.name = name;
        this.age = age;
        this.gpa = gpa;
        this.isEnrolled = true;
    }
    // method
    void study(){
        System.out.println(this.name + " is studying.");
    }
}


