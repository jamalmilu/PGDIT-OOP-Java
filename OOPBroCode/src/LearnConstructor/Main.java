package LearnConstructor;

public class Main {
    public static void main(String[] args) {
        // create a student object
        Student student1 = new Student("Jamal", 51,3.75);
        Student student2 = new Student("Kamal", 55,4.00);

        System.out.println(student1.name);
        System.out.println(student1.age);
        System.out.println(student1.gpa);
        System.out.println(student1.isEnrolled);

        System.out.println(student2.name);
        System.out.println(student2.age);
        System.out.println(student2.gpa);
        System.out.println(student2.isEnrolled);

        student1.study();
        student2.study();
    }
}
