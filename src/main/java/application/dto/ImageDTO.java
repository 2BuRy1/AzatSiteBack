package application.dto;


import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.util.Arrays;


@Data
@Entity
public class ImageDTO {


    public ImageDTO(){}

    public ImageDTO(String name, byte[] image){
        this.name = name;
        this.image = image;
    }

    private String name;

    private byte[] image;
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

}
