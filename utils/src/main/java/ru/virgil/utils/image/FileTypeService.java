package ru.virgil.utils.image;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
public class FileTypeService extends Tika {

    // todo: в утилиты
    public String getImageMimeType(byte[] content) {
        String mimeType = detect(content);
        String imageTypeRegex = "image/.*";
        if (!mimeType.matches(imageTypeRegex)) {
            throw new UnsupportedOperationException("It should be an image (%s). Given file: %s"
                    .formatted(imageTypeRegex, mimeType));
        }
        return mimeType;
    }

}
