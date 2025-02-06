package application.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads"; // Относительный путь

    public void createFile(String fileName, byte[] bytes) {
        File uploadDir = new File(UPLOAD_DIR);

        if (!uploadDir.exists()) {
            boolean dirCreated = uploadDir.mkdirs(); // Используем mkdirs()
            if (!dirCreated) {
                throw new RuntimeException("Не удалось создать директорию: " + UPLOAD_DIR);
            }
        }

        Path filePath = Path.of(UPLOAD_DIR, fileName);

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи файла: " + fileName, e);
        }
    }

    public File getFileByName(String fileName) {
        return new File(Path.of(UPLOAD_DIR, fileName).toString());
    }

    public void removeFileByName(String fileName) {
        File file = getFileByName(fileName);
        if (!file.exists()) {
            throw new RuntimeException("Файл не найден: " + fileName);
        }
        if (!file.delete()) {
            throw new RuntimeException("Не удалось удалить файл: " + fileName);
        }
    }
}