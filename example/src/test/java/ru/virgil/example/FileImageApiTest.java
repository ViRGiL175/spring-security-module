package ru.virgil.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.virgil.example.image.PrivateFileImageDto;
import ru.virgil.example.image.ImageService;
import ru.virgil.example.image.ImageMockService;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.utils.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void postPrivateImage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/private")
                        .file(imageMockService.mockAsMultipart()))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk());
    }

    @Test
    public void getPrivateImage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/image/private")
                        .file(imageMockService.mockAsMultipart()))
                .andExpect(status().isOk())
                .andReturn();
        PrivateFileImageDto privateFileImageDto = jackson.readValue(mvcResult.getResponse().getContentAsString(), PrivateFileImageDto.class);
        mockMvc.perform(get("/image/private/%s".formatted(privateFileImageDto.getUuid())))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk());
    }

    @Test
    public void getProtectedImage() throws Exception {
        mockMvc.perform(get("/image/protected/image.jpg"))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk());
    }

    @Test
    public void getPublicImage() throws Exception {
        mockMvc.perform(get("/image/public/image.jpg"))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk());
    }

    @AfterAll
    public void cleanUp() {
        // TODO при запуске _всех_ тестов не срабатывает, при запуска _одного_ – срабатывает
        imageService.cleanFolders();
    }
}
