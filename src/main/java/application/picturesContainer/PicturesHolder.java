package application.picturesContainer;

import application.services.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.Stream;


@Component
@Scope("singleton")
public class PicturesHolder {

    private final HashMap<String, String> pictures= new HashMap<>();

    @Autowired
    private FileService fileService;


    @Autowired
    private Logger logger;





    public void addToPictures(String name, String fullMultipartName){
        pictures.put(name, fullMultipartName);
    }

    public String getPictureByName(String name){
        return pictures.get(name);
    }



    @PostConstruct
    public void init(){

      Path path = Paths.get("uploads/");
	
	if (!Files.exists(path)) {
        try {
            // Пытаемся создать директорию, если её нет
            Files.createDirectories(path);
            System.out.println("Директория uploads создана");
        } catch (IOException e) {
            System.out.println("Не удалось создать директорию uploads: " + e.getMessage());
            return; // Выходим из метода, если не смогли создать
        }
    }


        try {
            Files.walk(path).
                    filter(file -> !file.getFileName().toString().equals("uploads"))
                    .forEach(file -> {
                        pictures.put(file.getFileName().toString().split("\\.")[0],
                                file.getFileName().toString()
                        );
                    });


        } catch (IOException e) {
            System.out.println("Директория не найднеа");
        }


    }


}
