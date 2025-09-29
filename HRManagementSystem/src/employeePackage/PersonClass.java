package employeePackage;

public class PersonClass {
    private String name;
    private int age;
    private String address;
    private String district;
    private String phone;

    //Constructor includes address now
    public PersonClass(String name, int age, String address, String district, String phone) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.district = district;
        this.phone = phone;
    }

    //Getters and Setters

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

