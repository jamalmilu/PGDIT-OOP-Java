package employeePackage;

public class Resume {
    //Create a person with all fields set(trimmed, no leading space)
    public PersonClass personalInformation = new PersonClass(
            "Md Jamal Hossain",50,"51 Kachari Road","Jhenaidah 7350", "01711236955"
    );

    public void printPersonalInformation(){
        System.out.println("=== Personal Information ===");
        System.out.println("Name        : "+personalInformation.getName());
        System.out.println("Age         : "+personalInformation.getAge());
        System.out.println("Address     : "+personalInformation.getAddress());
        System.out.println("District    : "+personalInformation.getDistrict());
        System.out.println("Phone       : "+personalInformation.getPhone());


    }
}
