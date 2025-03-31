package application.models;

import org.telegram.telegrambots.meta.api.objects.Message;

public class Start extends Command{
    public Start() {
        super("/start", "initial command");
    }

    @Override
    public String execute(Message message) {
        return "Добро пожаловать в азат-бота! Чтобы получить сводку по командам: напиши /help";
    }

}

