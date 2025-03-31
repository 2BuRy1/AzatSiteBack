package application.models;

import org.reflections.Reflections;

import java.sql.Ref;
import java.util.Set;

public class Help extends Command{


    public Help() {
        super("/help",
                "All available commands list");
    }

    @Override
    public String execute() {
        StringBuilder stringBuilder = new StringBuilder();

        Set<Class<? extends Command>> classes = new Reflections("application.models").getSubTypesOf(Command.class);
        classes.forEach(value -> {
            try {
                stringBuilder.append(value.newInstance().getName() + ": ").append(value.newInstance().getDescription()).append("\n");
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return stringBuilder.toString();
    }


}
