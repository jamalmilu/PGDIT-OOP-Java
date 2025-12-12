package LearnARRAY;

public class Main {
    public static void main(String[] args) {

        Car[] cars = {new Car("Mustang","Red"),
                new Car("Corvette", "Black"),
                new Car("Charger", "Blue")};
        for(Car car: cars){
            car.color = "black";
        }
        for(Car car: cars){
            car.drive();
        }
    }
}

