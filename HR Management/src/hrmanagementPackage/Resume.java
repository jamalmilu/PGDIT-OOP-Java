package hrmanagementPackage;

public class Resume {
    public Person personalInformation = new Person(
            "Md Jamal Hossain", 50,"51 Kachari Road", "Jhenaidah 7350", "01711236955"
    );

    public void printPersonalInformation(){
        System.out.println("=== Personal Information");
        System.out.println("Name                : "+this.personalInformation.getName());
        System.out.println("Age                 : "+this.personalInformation.getAge());
        System.out.println("Address             : "+this.personalInformation.getAddress());
        System.out.println("District            : "+this.personalInformation.getDistrict());
        System.out.println("Phone no            : "+this.personalInformation.getPhone_no());
    }
}
