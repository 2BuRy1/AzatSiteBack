package application.repository;

import application.dto.ImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageDTO, Long> {
    List<ImageDTO> findAll();
    Optional<ImageDTO> getImageDTOByName(String name);


}
