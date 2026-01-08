package JobApplicandDatabase;

public class JobApplicant {
    private String applicantId;
    private String applicantName;
    private int age;
    private String jobPost;
    private int yearsOfExperience;

    // Constructor
    public JobApplicant(String applicantId, String applicantName, int age,
                        String jobPost, int yearsOfExperience){
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.age = age;
        this.jobPost = jobPost;
        this.yearsOfExperience = yearsOfExperience;
    }

    // Getter and Setter methods


    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
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
    // Serialization for file storage

    @Override
    public String toString(){
        // Comma-separated format used by FileManager
        return applicantId + "," + applicantName + "," + age + "," + jobPost + "," + yearsOfExperience;
    }

    // Human-readable representation for console
    public String display(){
        return "ID: " + applicantId +
                " | Name: " + applicantName +
                " | Age: " + age +
                " | Job Post: " + jobPost +
                " | Experience: " + yearsOfExperience;
    }
}
