package jamalsModel;

public class Main {
    public static void main(String[] args){
        //Create an object p of type Person
        Person p = new Person();

        //Call setter to store values inside the object
        p.setName("Md Jamal Hossain");
        p.setAge(50);
        p.setAddress("Kaliganj, Jhenaidah 7350");
        p.setNID("1234567890");

        //Call getter and print the data
        System.out.println("Name: "+p.getName());
        System.out.println("Age: "+p.getAge());
        System.out.println("Address: "+p.getAddress());
        System.out.println("Age: "+p.checkNID());

    }
}
