import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadFile {

    private static final Logger logger = Logger.getLogger(ReadFile.class.getName());

    public Map<String, String> readFiles(String folderPath) {
        Map<String, String> contents = new HashMap<>();
        File folder = new File(folderPath);

        if (!folder.exists()) {
            logger.log(Level.SEVERE, "Folder does not exist: " + folderPath);
            return contents;
        }

        if (!folder.isDirectory()) {
            logger.log(Level.SEVERE, "Path is not a directory: " + folderPath);
            return contents;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            logger.log(Level.SEVERE, "Failed to list files in the folder: " + folderPath);
            return contents;
        }

        try {
            String canonicalFolderPath = folder.getCanonicalPath();

            for (File file : files) {
                if (file.isFile()) {
                    String canonicalFilePath = file.getCanonicalPath();
                    if (!canonicalFilePath.startsWith(canonicalFolderPath)) {
                        logger.log(Level.SEVERE, "File path is outside the folder: " + file.getName());
                        continue;
                    }

                    StringBuilder content = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "IO Exception while reading file: " + file.getName(), e);
                    }
                    contents.put(file.getName(), content.toString());
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Exception while processing folder: " + folderPath, e);
        }

        return contents;
    }
}