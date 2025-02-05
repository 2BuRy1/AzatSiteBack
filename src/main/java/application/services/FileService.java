package application.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class FileService {

    private static String UPLOAD_DIR = "/uploads";

    public void createFile(String fileName, byte[] bytes){

        Path filePath = Path.of(UPLOAD_DIR, fileName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public File getFileByName(String fileName){
        Path filePath = Path.of(UPLOAD_DIR, fileName);
        return new File(filePath.toString());

    }

    public void removeFileByName(String fileName){
        File file = getFileByName(fileName);
        file.delete();
    }



}
