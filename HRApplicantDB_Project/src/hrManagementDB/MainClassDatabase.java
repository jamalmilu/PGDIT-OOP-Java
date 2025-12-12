package hrManagementDB;

public class MainClassDatabase {
        public static void main(String[] args) {

            HRDatabase applicants_db = new HRDatabase();

            applicants_db.createTable();

            applicants_db.insertApplicant(1001, "Badruzzaman Liton", 35, "Senior Software Engineer");
            applicants_db.insertApplicant(1002, "Mahade Hasan", 28, "QA Engineering");
            applicants_db.insertApplicant(1003, "Jinia Islam", 38, "QA Engineering");

            System.out.println("\nAll applicants:");
            applicants_db.getApplicants();

            applicants_db.updateApplicant(1001, "Md Badruzzaman", 35, "Senior Software Engineer");

            applicants_db.deleteApplicant(1003);

            System.out.println("\nAfter updates:");
            applicants_db.getApplicants();
        }

}
