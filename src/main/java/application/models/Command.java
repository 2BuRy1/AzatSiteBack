package application.models;

import application.dto.ImageDTO;
import application.repository.ImageRepository;
import lombok.Data;
import lombok.Getter;

import java.awt.*;

@Data
public abstract class Command {

    protected ImageRepository imageRepository;

    protected final String name;

    protected final String description;

    public Command(String name, String description){
        this.name = name;
        this.description = description;
    }


    public abstract String execute();
}
