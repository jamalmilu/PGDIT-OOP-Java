package overriding;

public class Main {
    public static void main(String[] args){
        // Runtime polymorphism demonstration

        Animal myAnimal;

        myAnimal = new Dog("Buddy");
        myAnimal.makeSound();

        myAnimal = new Cat("Whiskers");
        myAnimal.makeSound();

        // Using covariant return type
        Dog parentDog = new Dog("Max");
        Dog puppy = parentDog.getChild();
        puppy.makeSound();
    }
}
