package overriding;
// Parent class
class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    // Method to be overridden
    public void makeSound() {
        System.out.println(name + " makes a generic animal sound");
    }

    public void eat() {
        System.out.println(name + " is eating");
    }

    // Method with covariant return type
    public Animal getChild() {
        return new Animal("Baby Animal");
    }
}

// Child class 1
class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    // Overriding makeSound method
    @Override
    public void makeSound() {
        System.out.println(name + " barks: Woof! Woof!");
    }

    // Covariant return type - returning Dog instead of Animal
    @Override
    public Dog getChild() {
        return new Dog("Puppy");
    }
}

// Child class 2
class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    // Overriding makeSound method
    @Override
    public void makeSound() {
        System.out.println(name + " meows: Meow! Meow!");
    }

    // New method specific to Cat (not overriding)
    public void climbTree() {
        System.out.println(name + " is climbing a tree");
    }
}

