package jamalsModel;

public class Person {
    // Private fields (attributes)
    private String name;
    private int age;
    private String address;
    private String NID;

    //Setter methods
    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }

    //Getter methods
    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    public String getAddress(){
        return address;
    }

    //Check NID(just an example: valid if length == 10
    public boolean checkNID(){
        return NID!= null && NID.length() == 10;
    }
}
