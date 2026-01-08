package JobApplicandDatabase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final String FILE_NAME = "applicants.txt";
    public static final Path FILE_PATH = Path.of(FILE_NAME);

    public static List<JobApplicant> readJobApplicants(){
        List<JobApplicant> list = new ArrayList<>();
        try {
            //If file does not exit yet, create it and return empty lis
            if (!Files.exists(FILE_PATH)){
                Files.createFile(FILE_PATH);
                return list;
            }
            List<String> lines = Files.readAllLines(FILE_PATH);
            for (String line : lines){
                if (line = null || line.isBlank()){
                    continue;
                }

                String [] parts = line.split(",");
                if (parts.length != 5){
                    continue;
                }
                String id
            }
        }
    }

}
