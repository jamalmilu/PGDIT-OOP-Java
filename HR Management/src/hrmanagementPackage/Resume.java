package employeePakage;

public class Resume {
    PersonClass personal_information= new PersonClass(
            "Jamal ", 51, " Kachari Road ","Jhenaidah ","01711236955"
    );
    educationalQualification education = new educationalQualification();

    public void setEducationInfo(){
        this.education.addDegreeInformation("SSC","MKSM School", "Jessore", 1990, 611);
        this.education.addDegreeInformation("HSC","MU College", "Jessore", 1992, 677);
        this.education.addDegreeInformation("BBS(Honours)","Accounting", "Islamic University", 1995, 820);
        this.education.addDegreeInformation("MBA","Accounting", "Islamic University", 2012, 3.70);

    }

    public void printPersonalInformation(){
        System.out.println(
                personal_information.getName() +
                        this.personal_information.getAge() +
                        this.personal_information.getAddress() +
                        this.personal_information.getDistrict() +
                        this.personal_information.getPhone ()
        );
    }
}
