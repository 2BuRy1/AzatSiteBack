package application.controllers;


import application.PicturesContainer.PicturesHolder;
import application.services.AzatMailSender;
import application.repository.RedisRepository;
import application.services.FileService;
import application.services.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class AzatController {

    private static final String UPLOAD_DIR = "uploads/";


    private final PicturesHolder picturesHolder;


    private final RedisRepository redisRepository;


    private final Logger logger;

    private final AzatMailSender azatMailSender;

    private final JwtConfiguration jwtConfiguration;

    private final FileService fileService;

    @Autowired
    public AzatController(PicturesHolder picturesHolder, RedisRepository redisRepository, Logger logger, AzatMailSender azatMailSender, JwtConfiguration jwtConfiguration, FileService fileService) {
        this.picturesHolder = picturesHolder;
        this.redisRepository = redisRepository;
        this.logger = logger;
        this.azatMailSender = azatMailSender;
        this.jwtConfiguration = jwtConfiguration;
        this.fileService = fileService;
    }


    @PostMapping("/api")
    public ResponseEntity<String> startVerify(@RequestPart("image") MultipartFile multipartFile, @RequestPart("name") String name) throws IOException {



        if (redisRepository.getData(name) == null) {

            redisRepository.saveImage(name, multipartFile.getBytes(), 12);

            picturesHolder.addPicture(name, multipartFile.getOriginalFilename());

            fileService.createFile(multipartFile.getOriginalFilename(), multipartFile.getBytes());


            azatMailSender.sendHtmlEmailWithAttachment("", "Новое фото", """
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
                    """, fileService.getFileByName(multipartFile.getOriginalFilename()));
            //TODO нагенерить токен + вставить его в ссылочку
            fileService.removeFileByName(multipartFile.getOriginalFilename());

            return ResponseEntity.ok("added 4 verifiation");

        }
        return ResponseEntity.badRequest().body("Picture already exists!!");


    }


    @GetMapping("/name")
    public ResponseEntity<Map<String, String>> getImage(@RequestParam("name") String name) throws IOException {
        byte[] array = picturesHolder.getByName(name);
        HashMap<String, String> result = new HashMap();
        if (array != null && array.length != 0) {


            result.put("image", new String(array));

            return ResponseEntity.ok(result);
        }
        result.put("error", "no such image");
        return ResponseEntity.badRequest().body(result);
    }


    @PostMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> adminPannel(@RequestParam("name") String name, @RequestParam("verified") boolean verified) {
        byte[] array = redisRepository.getData(name);
        if (array != null && array.length != 0) {

            if (verified) {


                String fileName = picturesHolder.getMultiPartName(name);
                Path filePath = Path.of(UPLOAD_DIR, fileName);

                try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    fos.write(array);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return ResponseEntity.ok("successfully added!");
            }

            picturesHolder.remove(name);
            redisRepository.remove(name);


        }

        return ResponseEntity.badRequest().body("No such image");
    }


}
