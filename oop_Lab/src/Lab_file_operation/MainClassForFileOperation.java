package Lab_file_operation;

public class MainClassForFileOperation {
    public static void main(String[] args){
        FileOperations myFileOperations = new FileOperations();

        // 1. Initial Write Operation
        String initialHeader = "Name, Roll, Semester, CourseId, Marks\n";
        myFileOperations.writeFile(initialHeader);

        // 2. Append Operations
        String student1 = "Jamal, 2504102, 2, PGD219, 85";
        String student2 = "Liton, 2504103, 2, PGD219, 72";
        String student3 = "Rahman, 2504108, 2,PGD219, 91";
        myFileOperations.appendToFile(student1);
        myFileOperations.appendToFile(student2);
        myFileOperations.appendToFile(student3);

        // 3. Read Operation (Before Update)
        String contentBeforeUpdate = myFileOperations.readFile();
        if(contentBeforeUpdate != null){
            System.out.println("File Content BEFORE Update:\n" + contentBeforeUpdate);
        }

        // 4. Update Operation (Change 'CS101' to 'OOP101')
        myFileOperations.updateFile("PGD219", "OOP101");

        // 5. Read Operation (After Update)
        String contentAfterUpdate = myFileOperations.readFile();
        if(contentAfterUpdate != null){
            System.out.println("File Content AFTER Update:\n" + contentAfterUpdate);
        }
    }
}