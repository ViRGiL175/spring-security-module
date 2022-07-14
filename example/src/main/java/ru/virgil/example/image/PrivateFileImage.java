package ru.virgil.example.image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.virgil.example.system.IdentifiedEntity;
import ru.virgil.example.user.UserDetails;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.nio.file.Path;

@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class PrivateFileImage extends IdentifiedEntity {

    @ManyToOne
    private UserDetails owner;
    private String fileLocation;

    public Path getFileLocation() {
        return Path.of(fileLocation);
    }

    public void setFileLocation(Path location) {
        this.fileLocation = location.toString();
    }

}
