package LearnARRAY;

import java.sql.SQLOutput;

public class Car {
    // initial variable
    String model;
    String color;

    // constructor
    Car(String model, String color){
        this.model = model;
        this.color = color;
    }
    // method
    void drive(){
        System.out.println("You drive the " + this.model + " " + this.color);
    }
}


