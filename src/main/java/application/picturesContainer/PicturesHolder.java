package application.picturesContainer;

import application.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Logger;


@Component
@Scope("singleton")
public class PicturesHolder {

    private final HashMap<String, String> picturesCash= new HashMap<>();

    private final HashMap<String, String> pictures= new HashMap<>();

    @Autowired
    private FileService fileService;


    @Autowired
    private Logger logger;



    public void addPictureToCash(String name, String fullMultipartName){
        picturesCash.put(name, fullMultipartName);
    }


    public byte[] getByNameFromCash(String name) throws IOException {

        File file = fileService.getFileByName(picturesCash.get(name));
        return Files.readAllBytes(file.toPath());
    }

    public String getMultiPartName(String name){
        return picturesCash.get(name);
    }

    public void remove(String name){
        picturesCash.remove(name);
    }

    public void addToPictures(String name, String fullMultipartName){
        pictures.put(name, fullMultipartName);
    }

    public String getPictureByName(String name){
        return pictures.get(name);
    }


}
