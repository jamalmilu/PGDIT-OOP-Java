package employeePakage;

public class PersonClass {

    //Private fields(arguments)
    private String name;
    private int age;
    private String address;
    private String district;
    private String phone;

    public PersonClass(String name, int age, String district, String phone) {
        this.name = name;
        this.age = age;
        this.district = district;
        this.phone = phone;
    }

    public PersonClass(String name, int age, String address, String district, String phone) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.district = district;
        this.phone = phone;
    }


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
