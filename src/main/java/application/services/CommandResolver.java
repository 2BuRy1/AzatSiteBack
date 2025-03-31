package application.services;

import application.models.Command;
import application.models.Help;
import jakarta.annotation.PostConstruct;
import javassist.tools.reflect.Reflection;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Component
public class CommandResolver {

    @Autowired
    Logger logger;

    public Optional<Command> resolve(String name) throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {

        Reflections reflections = new Reflections("application.models");
        Set<Class<? extends Command>> subClasses = reflections.getSubTypesOf(Command.class);
        for (var e : subClasses) {
            Method method = e.getMethod("getName");
            if (method.invoke(e.newInstance()).equals(name)) return Optional.of(e.newInstance());
        }
        return Optional.empty();
    }
}
