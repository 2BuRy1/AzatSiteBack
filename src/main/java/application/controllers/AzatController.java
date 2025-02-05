package application.controllers;


import application.PicturesContainer.PicturesHolder;
import application.configurations.AzatMailSender;
import application.dto.ImageDTO;
import application.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class AzatController {

    private static final String UPLOAD_DIR = "uploads/";


    private final PicturesHolder picturesHolder;


    private final RedisRepository redisRepository;


    private final Logger logger;

    private final AzatMailSender azatMailSender;

    @Autowired
    public AzatController(PicturesHolder picturesHolder, RedisRepository redisRepository, Logger logger, AzatMailSender azatMailSender) {
        this.picturesHolder = picturesHolder;
        this.redisRepository = redisRepository;
        this.logger = logger;
        this.azatMailSender = azatMailSender;
    }


    @PostMapping("/api")
    public ResponseEntity<String> startVerify(@RequestPart("image") MultipartFile multipartFile, @RequestPart("name") String name) throws IOException {

//        // Создаем директорию, если её нет
//        File uploadDir = new File(UPLOAD_DIR);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//
//        // Генерируем уникальное имя файла
//        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
//        Path filePath = Path.of(UPLOAD_DIR, fileName);
//
//        // Получаем байты из MultipartFile и записываем в файл
//        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
//            fos.write(multipartFile.getBytes());
//        }


        if (redisRepository.getData(name) == null) {
            Path filePath = Path.of(UPLOAD_DIR, multipartFile.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(multipartFile.getBytes());
            }
               logger.info(filePath.toString());
                redisRepository.saveImage(name, multipartFile.getBytes(), 12);
            File file = new File(filePath.toString());
                azatMailSender.sendHtmlEmailWithAttachment("gamemanfullvision@gmail.com", "Новое фото", """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>New Photo Added</title>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: #f4f4f4;
                                    text-align: center;
                                    padding: 20px;
                                }
                                .container {
                                    background: white;
                                    padding: 20px;
                                    border-radius: 10px;
                                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                    display: inline-block;
                                    max-width: 500px;
                                }
                                .photo-container {
                                    margin: 20px 0;
                                    text-align: center;
                                }
                                .photo-container img {
                                    max-width: 100%;
                                    border-radius: 10px;
                                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                }
                                .button {
                                    display: inline-block;
                                    padding: 10px 20px;
                                    font-size: 16px;
                                    color: white;
                                    background-color: #28a745;
                                    text-decoration: none;
                                    border-radius: 5px;
                                    margin-top: 20px;
                                }
                                .button:hover {
                                    background-color: #218838;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <h2>Новая фотография была добавлена!</h2>
                                <p>Для подтверждения добавления фотографии, пожалуйста, перейдите по ссылке ниже:</p>
                              <a href="my.itmo.ru">Gavnoo</a>
                            </div>
                        </body>
                        </html>
                        """, file);

        file.delete();

            return ResponseEntity.ok("added 4 verifiation");

        }
        return ResponseEntity.badRequest().body("Picture already exists!!");


    }
}
