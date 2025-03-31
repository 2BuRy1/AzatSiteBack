package application.models;

import org.reflections.Reflections;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.Set;
import java.util.stream.Collectors;

public class Help extends Command{


    public Help() {
        super("/help",
                "All available commands list");
    }

    @Override
    public String execute(Message message) {
        StringBuilder stringBuilder = new StringBuilder();

        Set<Class<? extends Command>> classes = new Reflections("application.models").getSubTypesOf(Command.class);
        classes = classes.stream().filter(value -> {
                    try {
                        Method method = value.getMethod("getName");
                        return method.invoke(value.newInstance()).toString().startsWith("/");
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                             InstantiationException e) {
                        throw new RuntimeException(e);
                    }


                }
                ).collect(Collectors.toSet());
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
