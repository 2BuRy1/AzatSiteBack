package application.controllers;

import application.dto.ImageDTO;
import application.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public AzatAdminController(RedisRepository redisRepository, Logger logger) {
        this.redisRepository = redisRepository;
        this.logger = logger;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllImages")
    public ResponseEntity<HashMap<String, ArrayList<ImageDTO>>> getAllPictures(Principal principal) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
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