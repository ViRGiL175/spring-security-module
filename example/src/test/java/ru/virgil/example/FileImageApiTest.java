package ru.virgil.example;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.virgil.example.image.ImageService;
import ru.virgil.example.image.PrivateImageFile;
import ru.virgil.example.image.PrivateImageFileDto;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.utils.fluent_request.RequestUtil;
import ru.virgil.utils.image.FileTypeService;
import ru.virgil.utils.image.ImageMockService;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileImageApiTest {

    private final ImageMockService<UserDetails, PrivateImageFile> imageMockService;
    private final ImageService imageService;
    private final RequestUtil requestUtil;
    private final FileTypeService fileTypeService;

    @Test
    @WithMockFirebaseUser
    void postPrivateImage() throws Exception {
        PrivateImageFileDto privateImageFileDto = (PrivateImageFileDto) requestUtil.postMultipart("/image/private")
                .file(imageMockService.mockAsMultipart())
                .receive(PrivateImageFileDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(privateImageFileDto).isNotNull();
    }

    @Test
    @WithMockFirebaseUser
    public void getPrivateImage() throws Exception {
        PrivateImageFileDto privateImageFileDto = (PrivateImageFileDto) requestUtil.postMultipart("/image/private")
                .file(imageMockService.mockAsMultipart())
                .receive(PrivateImageFileDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(privateImageFileDto).isNotNull();
        byte[] byteArray = (byte[]) requestUtil.get("/image/private/%s".formatted(privateImageFileDto.getUuid()))
                .receiveAsBytes()
                .and()
                .expect(status().isOk());
        Truth.assertThat(fileTypeService.getImageMimeType(byteArray)).contains("image/");
    }

    @Test
    @WithMockFirebaseUser
    public void getProtectedImage() throws Exception {
        byte[] byteArray = (byte[]) requestUtil.get("/image/protected/image.jpg")
                .receiveAsBytes()
                .and()
                .expect(status().isOk());
        Truth.assertThat(fileTypeService.getImageMimeType(byteArray)).contains("image/");
    }

    @Test
    public void getPublicImage() throws Exception {
        byte[] byteArray = (byte[]) requestUtil.get("/image/public/image.jpg")
                .receiveAsBytes()
                .and()
                .expect(status().isOk());
        Truth.assertThat(fileTypeService.getImageMimeType(byteArray)).contains("image/");
    }

    @Test
    @WithMockFirebaseUser
    void getNotExisting() throws Exception {
        requestUtil.get("/image/public/not_existing.jpg")
                .and()
                .expect(status().isNotFound());
        requestUtil.get("/image/protected/not_existing.jpg")
                .and()
                .expect(status().isNotFound());
        requestUtil.get("/image/protected/%s".formatted(UUID.randomUUID()))
                .and()
                .expect(status().isNotFound());
    }

    @AfterAll
    public void cleanUp() {
        // TODO при запуске _всех_ тестов не срабатывает, при запуска _одного_ – срабатывает
        imageService.cleanFolders();
    }
}
