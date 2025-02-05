package application.picturesContainer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;


@Component
@Scope("singleton")
public class PicturesHolder {

    private final HashMap<String, String> pictures= new HashMap<>();



    public void addPicture(String name, String fullMultipartName){
        pictures.put(name, fullMultipartName);
    }


    public byte[] getByName(String name) throws IOException {
        File file = new File (pictures.get(name));
        return Files.readAllBytes(file.toPath());
    }

    public String getMultiPartName(String name){
        return pictures.get(name);
    }

    public void remove(String name){
        pictures.remove(name);
    }


}
