package ru.virgil.utils.image;

import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import ru.virgil.utils.FakerUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

@RequiredArgsConstructor
public abstract class ImageMockService<Owner extends IBaseEntity, Image extends IPrivateImage<Owner>> {

    public static final String IMAGE_NAME = "image";
    private final ImageService<Owner, Image> imageService;
    private final FakerUtils fakerUtils;

    public Image mockImage(Owner owner) {
        try {
            return imageService.savePrivate(mockAsMultipart().getBytes(), IMAGE_NAME, owner);
        } catch (IOException e) {
            throw new MockImageException(e);
        }
    }

    public MockMultipartFile mockAsMultipart() {
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(fakerUtils.avatar().image()).openStream());
            return new MockMultipartFile(IMAGE_NAME, in);
        } catch (IOException e) {
            throw new MockImageException(e);
        }
    }
}
