package hrmanagementPackage;

public class Person {
    //Fields(arguments)
    private String name;
    private int age;
    private  String address;
    private String district;
    private String phone_no;

    //Constructor
    public Person(String name, int age, String address, String district, String phone_no) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.district = district;
        this.phone_no = phone_no;
    }

    //Getter and Setter methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
