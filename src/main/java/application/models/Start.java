package application.models;

public class Start extends Command{
    public Start() {
        super("/start", "initial command");
    }

    @Override
    public String execute() {
        return "Добро пожаловать в азат-бота! Чтобы получить сводку по командам: напиши /help";
    }
}
