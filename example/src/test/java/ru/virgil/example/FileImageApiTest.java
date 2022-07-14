package ru.virgil.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.virgil.utils.FileTypeService;
import ru.virgil.example.image.ImageMockService;
import ru.virgil.example.image.ImageService;
import ru.virgil.example.image.PrivateFileImageDto;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.utils.TestUtils;
import ru.virgil.utils.fluent_request.RequestUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileImageApiTest {

    private final MockMvc mockMvc;
    private final ObjectMapper jackson;
    private final TestUtils testUtils;

    private final ImageMockService imageMockService;

    private final ImageService imageService;
    private final RequestUtil requestUtil;
    private final FileTypeService fileTypeService;

    @Test
    void postPrivateImage() throws Exception {
        PrivateFileImageDto privateFileImageDto = (PrivateFileImageDto) requestUtil.postMultipart("/image/private")
                .file(imageMockService.mockAsMultipart())
                .receive(PrivateFileImageDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(privateFileImageDto).isNotNull();

        //        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/private")
        //                        .file(imageMockService.mockAsMultipart()))
        //                .andDo(testUtils::printResponse)
        //                .andExpect(status().isOk());

    }

    @Test
    public void getPrivateImage() throws Exception {
        PrivateFileImageDto privateFileImageDto = (PrivateFileImageDto) requestUtil.postMultipart("/image/private")
                .file(imageMockService.mockAsMultipart())
                .receive(PrivateFileImageDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(privateFileImageDto).isNotNull();

        byte[] byteArray = (byte[]) requestUtil.get("/image/private/%s".formatted(privateFileImageDto.getUuid()))
                .receiveAsBytes()
                .and()
                .expect(status().isOk());
        Truth.assertThat(fileTypeService.getImageMimeType(byteArray)).contains("image/");

        //        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/image/private")
        //                        .file(imageMockService.mockAsMultipart()))
        //                .andExpect(status().isOk())
        //                .andReturn();
        //        PrivateFileImageDto privateFileImageDto = jackson.readValue(mvcResult.getResponse().getContentAsString(), PrivateFileImageDto.class);
        //        mockMvc.perform(get("/image/private/%s".formatted(privateFileImageDto.getUuid())))
        //                .andDo(testUtils::printResponse)
        //                .andExpect(status().isOk());
    }

    @Test
    public void getProtectedImage() throws Exception {
        byte[] byteArray = (byte[]) requestUtil.get("/image/protected/image.jpg")
                .receiveAsBytes()
                .and()
                .expect(status().isOk());
        Truth.assertThat(fileTypeService.getImageMimeType(byteArray)).contains("image/");

        //        mockMvc.perform(get("/image/protected/image.jpg"))
        //                .andDo(testUtils::printResponse)
        //                .andExpect(status().isOk());
    }

    @Test
    public void getPublicImage() throws Exception {
        byte[] byteArray = (byte[]) requestUtil.get("/image/public/image.jpg")
                .receiveAsBytes()
                .and()
                .expect(status().isOk());
        Truth.assertThat(fileTypeService.getImageMimeType(byteArray)).contains("image/");

        //        mockMvc.perform(get("/image/public/image.jpg"))
        //                .andDo(testUtils::printResponse)
        //                .andExpect(status().isOk());
    }

    @AfterAll
    public void cleanUp() {
        // TODO при запуске _всех_ тестов не срабатывает, при запуска _одного_ – срабатывает
        imageService.cleanFolders();
    }
}
