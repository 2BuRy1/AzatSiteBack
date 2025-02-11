package application.controllers;


import application.dto.ImageDTO;
import application.repository.RedisRepository;
import io.netty.handler.codec.base64.Base64Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AzatAdminController {


    private final RedisRepository redisTemplate;

    private final Logger logger;

    @Autowired
    public AzatAdminController(RedisRepository redisTemplate, Logger logger) {
        this.redisTemplate = redisTemplate;
        this.logger = logger;
    }

    @GetMapping("/getAllImages")
    public ResponseEntity<Map<String,ArrayList<ImageDTO>>> getAllPictures(){
        ArrayList<String> keys = (ArrayList<String>) redisTemplate.getAllKeys();
        ArrayList<ImageDTO> images = new ArrayList<>();
        for(var e: keys){
            images.add(new ImageDTO(e, Base64.getEncoder().encode(redisTemplate.getData(e).getBytes())));

        }

        logger.log(Level.INFO, String.valueOf(keys.size()));

        HashMap<String, ArrayList<ImageDTO>> result = new HashMap<>();

        result.put("data", images);

        return ResponseEntity.ok(result);
    }

}
