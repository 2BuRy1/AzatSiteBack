package application.dto;


import lombok.Data;

import java.util.Arrays;


@Data
public class ImageDTO {

    private final String name;

    private final byte[] image;

}
