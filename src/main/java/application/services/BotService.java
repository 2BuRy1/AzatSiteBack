package application.services;

import application.dto.BotDTO;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotService extends TelegramLongPollingBot {



    @Autowired
    private BotDTO botDTO;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().hasText()){

            send(update.getMessage().getChatId(), "Привет, %s".formatted(update.getMessage().getChat().getFirstName()));

        }
    }


    private void send(long chatId, String message){
        var reply = new SendMessage();
        reply.setText(message);
        reply.setChatId(chatId);

        try {
            execute(reply);
        } catch (TelegramApiException e) {
           //TODO logging;
        }

    }

    @Override
    public String getBotUsername() {
        return botDTO.getBotName() ;
    }

    @Override
    public String getBotToken(){
        return botDTO.getBotToken();
    }

}
