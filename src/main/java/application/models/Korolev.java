package application.models;

import org.telegram.telegrambots.meta.api.objects.Message;

public class Korolev extends Command{
    public Korolev() {
        super("королев", "просто так уоу");
    }


    @Override
    public  String execute(Message message) {
        StringBuilder stringBuilder = new StringBuilder();
        if (message.getText().startsWith("королев")) {
            var fields = message.getFrom().getClass().getDeclaredFields();
            for (var e : fields) {
                stringBuilder.append(e);
            }

            System.out.println(message);

        }
        return "нееее " + stringBuilder.toString();
    }

}
