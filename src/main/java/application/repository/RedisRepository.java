package application.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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


    public Object getData(String key){
        return redisTemplate.opsForValue().get(key);
    }


}
