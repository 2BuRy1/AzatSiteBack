package application.services;

import application.dto.BotDTO;
import application.models.Command;
import application.models.GetImages;
import application.repository.ImageRepository;
import lombok.SneakyThrows;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.util.Optional;

@Component
public class BotService extends TelegramLongPollingBot {


    @Autowired
    private CommandResolver commandResolver;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BotDTO botDTO;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().hasText()){
            Optional<Command> command = commandResolver.resolve(update.getMessage().getText());
            if (command.isPresent()){
                Command execCommand = command.get();
                if(execCommand instanceof GetImages) command.get().setImageRepository(imageRepository);
                var result = command.get().execute();
                System.out.println(result);
                send(update.getMessage().getChatId(), result);
            }
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
