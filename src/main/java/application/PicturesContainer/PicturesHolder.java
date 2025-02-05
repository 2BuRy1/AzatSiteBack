package application.PicturesContainer;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
public class PicturesHolder {

    private static HashMap<String, ClassPathResource> pictures= new HashMap();

    public PicturesHolder(){
        pictures.put("Тупой славик", new ClassPathResource("pictures/2025-01-08 00.03.26.jpg"));
        pictures.put("Крутой славик", new ClassPathResource("pictures/cool.jpg"));
    }


    public ClassPathResource getByName(String name){
        return pictures.get(name);
    }


}
