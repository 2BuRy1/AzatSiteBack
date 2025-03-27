package application.configurations;

import application.services.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfiguration {




//    @Bean
//    public TelegramBotsApi telegramBotsApi(BotService botService) throws TelegramApiException {
//        var api = new TelegramBotsApi(DefaultBotSession.class);
//        api.registerBot(botService);
//        return api;
//    }
}
