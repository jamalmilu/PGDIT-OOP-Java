package Lab_file_operataion;

import java.io.File;

public class MainClassForFileOperation {

    //    Main function
    public static void main(String[] args) {

        FileOperations myFileOperations = new FileOperations();

        String initialContent = "Name, roll, semester, course_id, marks\n";
        // 1. WRITE
        myFileOperations.writeFile(initialContent);

        //2. Append
        myFileOperations.appendToFile("Abdur rahman, 2504108, 2, PGD217, 76");
        myFileOperations.appendToFile("Badruzzaman, 2504110, 2, PGD217, 86");
        myFileOperations.appendToFile("Hasina Akter, 2504111, 2, PGD217, 75");



        // 3. READ and display
        String readContent = myFileOperations.readFile();
        System.out.println("\n[Current File Content]\n" + readContent);

        // 4. UPDATE
        myFileOperations.updateFile("important for update", "now modified");

        // 5. READ and verify
        String verifiedContent = myFileOperations.readFile();
        System.out.println("\n[Verified File Content After Update]\n" + verifiedContent);

//        // Cleanup: Delete the file after the demonstration
//        File file = new File(myFileOperations.FILE_NAME);
//        if (file.delete()) {
//            System.out.println("\nSuccessfully deleted the demo file: " + myFileOperations.FILE_NAME);
//        }
    }
}
