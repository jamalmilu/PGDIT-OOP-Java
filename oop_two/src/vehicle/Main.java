import vehicle.Car;

public class Main {
    public static void main(String[] args) {
        //Create a Car object
        Car c1 = new Car();

//Calls setters to store values inside the objects.
c1.setBrand("Toyota");
c1.setModel("Corolla");
c1.setYear(2020);

//Calls getters and print the data
System.out.println("Brand: " + c1.getBrand());
System.out.println("Model: "+ c1.getModel());
System.out.println("Year: "+ c1.getYear());

    }
}