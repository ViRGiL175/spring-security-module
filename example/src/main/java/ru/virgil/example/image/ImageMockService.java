package ru.virgil.example.image;

import org.springframework.stereotype.Service;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.FakerUtils;
import ru.virgil.utils.image.ImageService;

@Service
public class ImageMockService extends ru.virgil.utils.image.ImageMockService<UserDetails, PrivateImageFile> {

    public ImageMockService(ImageService<UserDetails, PrivateImageFile> imageService, FakerUtils fakerUtils) {
        super(imageService, fakerUtils);
    }
}
