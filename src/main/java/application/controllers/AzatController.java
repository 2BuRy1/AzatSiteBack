package application.controllers;


import application.dto.ImageDTO;

import application.repository.ImageRepository;
import application.services.AzatMailSender;
import application.repository.RedisRepository;
import application.services.FileService;
import application.services.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class AzatController {

    private static final String UPLOAD_DIR = "uploads/";



    private final RedisRepository redisRepository;



    @Value("${username}")
    private String username;

    @Value("${email.to}")
    private String emailTo;

    private final Logger logger;

    private final AzatMailSender azatMailSender;

    private final JwtConfiguration jwtConfiguration;

    private final FileService fileService;

    private final ImageRepository imageRepository;


    @Autowired
    public AzatController( RedisRepository redisRepository, Logger logger, AzatMailSender azatMailSender, JwtConfiguration jwtConfiguration, FileService fileService, ImageRepository imageRepository) {

        this.redisRepository = redisRepository;
        this.logger = logger;
        this.azatMailSender = azatMailSender;
        this.jwtConfiguration = jwtConfiguration;
        this.fileService = fileService;
        this.imageRepository = imageRepository;
    }


    @PostMapping("/endpoint")
    public ResponseEntity<String> startVerify(@RequestPart("image") MultipartFile multipartFile, @RequestPart("name") String name) throws IOException {



        if (redisRepository.getData(name) == null) {

            redisRepository.saveImage(name, multipartFile.getBytes(), 12);

            fileService.createFile(multipartFile.getOriginalFilename(), multipartFile.getBytes());


            azatMailSender.sendHtmlEmailWithAttachment(emailTo, "Новое фото",
                    String.format(
                            "<html>" +
                                    "<body>" +
                                    "<h2>Новая фотография была добавлена!</h2>" +
                                    "<p>Для подтверждения добавления фотографии, пожалуйста, перейдите по ссылке ниже:</p>" +
                                    "<a href=\"http://se.ifmo.shop:8089/admin?token=%s\">Перейти для подтверждения</a>" +
                                    "</body>" +
                                    "</html>",
                            jwtConfiguration.generateToken(username)),
                    fileService.getFileByName(multipartFile.getOriginalFilename())
            );
           //   fileService.removeFileByName(multipartFile.getOriginalFilename());

            return ResponseEntity.ok("added 4 verifiation");

        }
        return ResponseEntity.badRequest().body("Picture already exists!!");


    }


    @GetMapping("/name")
    public ResponseEntity<?> getImage(@RequestParam("data") String name) throws IOException {
        Optional<ImageDTO> imageDTO = imageRepository.getImageDTOByName(name);
        if (imageDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error", "No such image"));
        }

        byte[] fileBytes = imageDTO.get().getImage();
        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                .body(resource);
    }




}
