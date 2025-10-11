package employeePakage;

public class MainClass {
    public static void main(String[] args) {
        System.out.println("Welcome to HR Team");

        Resume applicant_1 = new Resume();
        applicant_1.printPersonalInformation();

        System.out.println("Age: "+applicant_1.personal_information.getAge());
        if (applicant_1.personal_information.getAge()>30) {
            System.out.println("Your age is: " + applicant_1.personal_information.getAge());
        }else{
                System.out.println("Not permitted");
            }

        applicant_1.setEducationInfo();

    //    applicant_1.education.printAllEducationalInformation();
        int passingYearDegree = applicant_1.education.getPassingYearForDegree("SSC");
        if (passingYearDegree < 2010){
            System.out.println("You are eligible");
        } else {
            System.out.println("You are too young to eligible");
        }
        }

    }

