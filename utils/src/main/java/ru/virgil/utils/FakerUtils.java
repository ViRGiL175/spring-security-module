package ru.virgil.utils;


import lombok.experimental.StandardException;
import net.datafaker.Avatar;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class FakerUtils extends Faker {

    public Avatar avatar() {
        return getProvider(CustomAvatar.class, () -> new CustomAvatar(this));
    }

    public CustomImage image() {
        return getProvider(CustomImage.class, CustomImage::new);
    }

    @StandardException
    protected static class CustomFakerException extends RuntimeException {

    }

    protected static class CustomImage {

        public static final int DEFAULT_SIZE = 256;
        private final URI baseUri;

        private CustomImage() {
            try {
                baseUri = new URI("https://picsum.photos");
            } catch (URISyntaxException e) {
                throw new CustomFakerException(e);
            }
        }

        public String url() {
            return baseUri.resolve(String.valueOf(DEFAULT_SIZE)).toString();
        }

        public String url(int size) {
            return baseUri.resolve(String.valueOf(size)).toString();
        }

        public String url(int height, int width) {
            return baseUri.resolve(String.valueOf(height)).resolve(String.valueOf(width)).toString();
        }

    }

    protected static class CustomAvatar extends Avatar {

        private static final String IMAGE = "https://picsum.photos/200";

        protected CustomAvatar(Faker faker) {
            super(faker);
        }

        @Override
        public String image() {
            return IMAGE;
        }
    }
}
