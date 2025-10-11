package employeePakage;

import java.util.ArrayList;

public class educationalQualification {

    private ArrayList<String> degreeList = new ArrayList<>();
    private ArrayList<String> departmentOrGroupList = new ArrayList<>();
    private ArrayList<String> universityOrBoardList = new ArrayList<>();
    private ArrayList<Integer> passingYearList = new ArrayList<>();
    private ArrayList<Double> gpaOrMarksList = new ArrayList<>();

    public void addDegreeInformation(String degree, String dept, String univOrBoard, int year, double marks) {
        this.degreeList.add(degree);
        this.departmentOrGroupList.add(dept);
        this.universityOrBoardList.add(univOrBoard);
        this.passingYearList.add(year);
        this.gpaOrMarksList.add(marks);
    }

    public void printDegrees() {
        for (String degree : degreeList) {
            System.out.println(degree);
        }
    }

    public void printAllEducationalInformation() {
        for (int index = 0; index < degreeList.size(); index++) {
            System.out.println("Degree: " + degreeList.get(index));
            System.out.println("Department/Group: " + departmentOrGroupList.get(index));
            System.out.println("University/Board: " + universityOrBoardList.get(index));
            System.out.println("Passing Year: " + passingYearList.get(index));
            System.out.println("GPA or Marks: " + gpaOrMarksList.get(index));

        }
    }

    public int getPassingYearForDegree(String degree) {
        int targetIndex = -1;
        for (int index = 0; index < degreeList.size(); index++) {
            if (degreeList.get(index).equalsIgnoreCase(degree)) {
                targetIndex = index;
                break;
            }
        }
        System.out.println("Your passing year of " + degree);
        return passingYearList.get(targetIndex);
    }

    public double getGpaOrMarks(String degree){
        int targetIndex = -1;
        for (int index = 0; index < degreeList.size(); index++) {
            if (degreeList.get(index).equalsIgnoreCase(degree)) {
                targetIndex = index;
                break;
            }
        }
        System.out.println("Your passing year of " + degree);
        return passingYearList.get(targetIndex);
    }
}
