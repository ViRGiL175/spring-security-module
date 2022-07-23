package ru.virgil.example.image;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.image.FileTypeService;

@Service
public class ImageService extends ru.virgil.utils.image.ImageService<UserDetails, PrivateImageFile> {

    public ImageService(
            ResourceLoader resourceLoader,
            PrivateImageRepository privateImageRepository,
            FileTypeService fileTypeService
    ) {
        super(resourceLoader, privateImageRepository, fileTypeService);
    }

    @Override
    protected PrivateImageFile createEmptyPrivateImage() {
        return new PrivateImageFile();
    }
}
