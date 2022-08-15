package ru.virgil.utils.image;

import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import ru.virgil.utils.FakerUtils;

import javax.annotation.PreDestroy;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

@RequiredArgsConstructor
public abstract class ImageMockService<Owner extends IBaseEntity, Image extends IPrivateImage<Owner>> {

    public static final String IMAGE_NAME = "image";
    protected final ImageService<Owner, Image> imageService;
    protected final FakerUtils fakerUtils;
    protected final ImageProperties imageProperties;

    public Image mockImage(Owner owner) {
        try {
            return imageService.savePrivate(mockAsMultipart().getBytes(), IMAGE_NAME, owner);
        } catch (IOException e) {
            throw new ImageException(e);
        }
    }

    public MockMultipartFile mockAsMultipart() {
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(fakerUtils.avatar().image()).openStream());
            return new MockMultipartFile(IMAGE_NAME, in);
        } catch (IOException e) {
            throw new ImageException(e);
        }
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        if (imageProperties.cleanOnDestroy()) {
            imageService.cleanFolders();
        }
    }
}
