package application.configurations;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class MainConfiguration {


    @Value("${username}")
    private String username;

    @Bean
    public UserDetailsManager userDetailsManager(){
        User user = (User) User.builder().username(username).password("").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user);
    }



    @Bean
    public Logger configureLogger(){
        var logger = Logger.getLogger(this.getClass().getName());
        logger.setLevel(Level.INFO);
        return logger;
    }

    @Bean
    public HttpSessionSecurityContextRepository securityContextRepository(){

        return new HttpSessionSecurityContextRepository();
    }


    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Используем сериализаторы для работы с байтами
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(RedisSerializer.byteArray());

        return template;
    }


    }

