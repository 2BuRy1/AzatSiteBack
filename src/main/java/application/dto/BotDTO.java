package application.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class BotDTO {

    @Value("${bot.name}")
    private String botName;


    @Value("${bot.token}")
    private String botToken;


}
