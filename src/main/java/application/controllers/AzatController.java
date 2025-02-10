package application.controllers;


import application.picturesContainer.PicturesHolder;
import application.services.AzatMailSender;
import application.repository.RedisRepository;
import application.services.FileService;
import application.services.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
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


    @PostMapping("/endpoint")
    public ResponseEntity<String> startVerify(@RequestPart("image") MultipartFile multipartFile, @RequestPart("name") String name) throws IOException {



        if (redisRepository.getData(name) == null) {

            redisRepository.saveImage(name, multipartFile.getBytes(), 12);

            picturesHolder.addPictureToCash(name, multipartFile.getOriginalFilename());

            fileService.createFile(multipartFile.getOriginalFilename(), multipartFile.getBytes());


//            azatMailSender.sendHtmlEmailWithAttachment("gamemanfullvision@gmail.com", "Новое фото", """
//                    <!DOCTYPE html>
//                    <html lang="en">
//                    <head>
//                        <meta charset="UTF-8">
//                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                        <title>New Photo Added</title>
//                        <style>
//                            body {
//                                font-family: Arial, sans-serif;
//                                background-color: #f4f4f4;
//                                text-align: center;
//                                padding: 20px;
//                            }
//                            .container {
//                                background: white;
//                                padding: 20px;
//                                border-radius: 10px;
//                                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
//                                display: inline-block;
//                                max-width: 500px;
//                            }
//                            .photo-container {
//                                margin: 20px 0;
//                                text-align: center;
//                            }
//                            .photo-container img {
//                                max-width: 100%;
//                                border-radius: 10px;
//                                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
//                            }
//                            .button {
//                                display: inline-block;
//                                padding: 10px 20px;
//                                font-size: 16px;
//                                color: white;
//                                background-color: #28a745;
//                                text-decoration: none;
//                                border-radius: 5px;
//                                margin-top: 20px;
//                            }
//                            .button:hover {
//                                background-color: #218838;
//                            }
//                        </style>
//                    </head>
//                    <body>
//                        <div class="container">
//                            <h2>Новая фотография была добавлена!</h2>
//                            <p>Для подтверждения добавления фотографии, пожалуйста, перейдите по ссылке ниже:</p>
//                          <a href="my.itmo.ru">Gavnoo</a>
//                        </div>
//                    </body>
//                    </html>
//                    """, fileService.getFileByName(multipartFile.getOriginalFilename()));
//            //TODO нагенерить токен + вставить его в ссылочку
//            //fileService.removeFileByName(multipartFile.getOriginalFilename());

            return ResponseEntity.ok("added 4 verifiation");

        }
        return ResponseEntity.badRequest().body("Picture already exists!!");


    }


    @GetMapping("/name")
    public ResponseEntity<?> getImage(@RequestParam("data") String name) throws IOException {
        String filePath = "uploads/" + picturesHolder.getMultiPartName(name);
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error", "No such image"));
        }

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Или IMAGE_PNG
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                .body(resource);
    }


    @PostMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> adminPannel(@RequestParam("name") String name, @RequestParam("verified") boolean verified) {
        byte[] array = redisRepository.getData(name).getBytes(StandardCharsets.UTF_8);
        if (array != null && array.length != 0) {

            if (verified) {


                String fileName = picturesHolder.getMultiPartName(name);

                fileService.createFile(fileName, array);
                return ResponseEntity.ok("successfully added!");
            }

            picturesHolder.remove(name);
            redisRepository.remove(name);


        }

        return ResponseEntity.badRequest().body("No such image");
    }


}
