package application.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisRepository {


    private final RedisTemplate redisTemplate;


    @Autowired
    public RedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveImage(String key, byte[] image, long ttl){
        redisTemplate.opsForValue().set(key, image, ttl, TimeUnit.HOURS);

    }


    public byte[] getData(String key){
        return (byte[]) redisTemplate.opsForValue().get(key);
    }

    public List<String> getAllKeys() {
        List<String> keys = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(100).build();
        try (var cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add((String) cursor.next());
            }
        }
        return keys;
    }


    public void remove(String key){
        redisTemplate.delete(key);
    }



}
