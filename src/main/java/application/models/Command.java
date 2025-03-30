package application.models;

import application.dto.ImageDTO;
import lombok.Data;
import lombok.Getter;
@Data
public abstract class Command {


    private final String name;

    private final String description;

    private String result;

    public Command(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void setRepository(ImageDTO imageDTO) {

    }

    public abstract String execute();
}
