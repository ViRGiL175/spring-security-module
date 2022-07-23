package ru.virgil.example.image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.system.IdentifiedEntity;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.image.IPrivateImage;

import javax.persistence.ManyToOne;
import java.nio.file.Path;

@javax.persistence.Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class PrivateImageFile extends IdentifiedEntity implements IPrivateImage<UserDetails> {

    @ManyToOne
    private UserDetails owner;
    private String fileLocation;

    @Override
    public Path getFileLocation() {
        return Path.of(fileLocation);
    }

    @Override
    public void setFileLocation(Path location) {
        this.fileLocation = location.toString();
    }

}
