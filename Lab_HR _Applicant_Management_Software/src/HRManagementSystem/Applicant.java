package HRManagementSystem;

/**
 * Simple model class representing one job applicant.
 */
public class Applicant {

    // --- Fields ---
    private String applicantId;
    private String name;
    private int age;
    private String jobPost;
    private int yearsOfExperience;

    // --- Constructor ---
    public Applicant(String applicantId, String name, int age, String jobPost, int yearsOfExperience) {
        this.applicantId = applicantId;
        this.name = name;
        this.age = age;
        this.jobPost = jobPost;
        this.yearsOfExperience = yearsOfExperience;
    }

    // --- Getters and setters ---

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
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

    public String getJobPost() {
        return jobPost;
    }

    public void setJobPost(String jobPost) {
        this.jobPost = jobPost;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    // --- Serialization for file storage ---
    @Override
    public String toString() {
        // Comma-separated format used by FileHandler
        return applicantId + "," + name + "," + age + "," + jobPost + "," + yearsOfExperience;
    }

    // --- Human-readable representation for console ---
    public String display() {
        return "ID: " + applicantId +
                " | Name: " + name +
                " | Age: " + age +
                " | Job Post: " + jobPost +
                " | Experience: " + yearsOfExperience + " years";
    }
}
