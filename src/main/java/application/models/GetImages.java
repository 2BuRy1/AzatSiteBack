package application.models;

import application.repository.ImageRepository;
import org.telegram.telegrambots.meta.api.objects.Message;

public class GetImages extends Command{


    public GetImages() {
        super("/images", "returns all image names ");
    }



    @Override
    public String execute(Message message) {
        StringBuilder stringBuilder = new StringBuilder();
        imageRepository.findAll()
                .forEach(value -> stringBuilder.append(value.getName())
                        .append("\n")
                );
        return stringBuilder.toString();
    }
}
