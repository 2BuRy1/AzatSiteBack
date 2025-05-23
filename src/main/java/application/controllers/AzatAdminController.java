package application.controllers;

import application.dto.ApprovementDTO;
import application.dto.ImageDTO;
import application.repository.ImageRepository;
import application.repository.RedisRepository;
import application.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AzatAdminController {

    private final RedisRepository redisRepository;

    private final Logger logger;

    private final FileService fileService;


    private final ImageRepository imageRepository;

    @Autowired
    public AzatAdminController(RedisRepository redisRepository, Logger logger, FileService fileService, ImageRepository imageRepository) {
        this.redisRepository = redisRepository;
        this.logger = logger;
        this.fileService = fileService;
        this.imageRepository = imageRepository;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve")
    public ResponseEntity<String> approveImage(@RequestBody ApprovementDTO approvementDTO){

        String imageName = approvementDTO.getName();

        if(approvementDTO.isStatus()){
            imageRepository.save(new ImageDTO(imageName, redisRepository.getData(approvementDTO.getName())));
            fileService.createFile(imageName + ".png", redisRepository.getData(approvementDTO.getName()));
            redisRepository.remove(approvementDTO.getName());
        }
        redisRepository.remove(approvementDTO.getName());
        return ResponseEntity.status(200).body("Ну чета закинули");
    }




    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllImages")
    public ResponseEntity<HashMap<String, ArrayList<ImageDTO>>> getAllPictures() {
        ArrayList<String> keys = (ArrayList<String>) redisRepository.getAllKeys();
        ArrayList<ImageDTO> images = new ArrayList<>();

        for (String key : keys) {
            byte[] imageBytes = redisRepository.getData(key);
            images.add(new ImageDTO(key, imageBytes));
        }

        logger.log(Level.INFO, "Количество картинок: " + keys.size());

        HashMap<String, ArrayList<ImageDTO>> result = new HashMap<>();
        result.put("data", images);

        return ResponseEntity.ok(result);
    }
}