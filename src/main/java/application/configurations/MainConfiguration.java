package application.configurations;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class MainConfiguration {


    @Bean
    public UserDetailsManager userDetailsManager(){
        User user = (User) User.builder().username("allah").password("mep").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user);
    }


    @Bean
    public Logger configureLogger(){
        var logger = Logger.getLogger(this.getClass().getName());
        logger.setLevel(Level.INFO);
        return logger;
    }


        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);

            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

            return template;
        }



    }

