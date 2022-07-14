package ru.virgil.example.image;

import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import ru.virgil.example.user.UserDetails;
import ru.virgil.utils.FakerUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class ImageMockService {

    // todo: в утилиты
    private final ImageService imageService;
    private final FakerUtils fakerUtils;

    public PrivateFileImage mockImage(UserDetails owner) {
        try {
            return imageService.savePrivate(mockAsMultipart().getBytes(), "image", owner);
        } catch (IOException e) {
            throw new MockImageException(e);
        }
    }

    public MockMultipartFile mockAsMultipart() {
        try {
            String name = "image";
            BufferedInputStream in = new BufferedInputStream(new URL(fakerUtils.avatar().image()).openStream());
            return new MockMultipartFile(name, in);
        } catch (IOException e) {
            throw new MockImageException(e);
        }
    }
}
