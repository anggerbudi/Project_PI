import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
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

        for (File file : files) {
            if (file.isFile()) {
                StringBuilder content = new StringBuilder();
                try (Scanner input = new Scanner(file)) {
                    while (input.hasNextLine()) {
                        content.append(input.nextLine()).append("\n");
                    }
                } catch (NoSuchElementException e) {
                    logger.log(Level.SEVERE, "File improperly formed: " + file.getName(), e);
                } catch (IllegalStateException e) {
                    logger.log(Level.SEVERE, "Error reading from file: " + file.getName(), e);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "IO Exception while reading file: " + file.getName(), e);
                }
                contents.put(file.getName(), content.toString());
            }
        }
        return contents;
    }
}