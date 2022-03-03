package ru.virgil.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.virgil.example.box.BoxController;
import ru.virgil.example.box.BoxDto;
import ru.virgil.example.box.BoxService;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.example.util.WithMockFirebaseUser;
import ru.virgil.utils.TestUtils;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoxApiTest {

    public static final int BOX_PAGE = 0;
    public static final int BOX_PAGE_SIZE = 10;
    private final MockMvc mockMvc;
    private final BoxService boxService;
    private final UserDetailsService userDetailsService;
    private final TestUtils testUtils;
    private final ObjectMapper jackson;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box")
                        .queryParam(BoxController.PAGE_PARAM, String.valueOf(BOX_PAGE))
                        .queryParam(BoxController.PAGE_SIZE_PARAM, String.valueOf(BOX_PAGE_SIZE)))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // todo: извлечение коллекции
        Truth.assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    void get() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/%s".formatted(randomBoxUuid())))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        BoxDto boxDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(boxDto.getWeight()).isAtLeast(10);
    }

    @Test
    void create() throws Exception {
        BoxDto testDto = new BoxDto();
        String testValue = "CREATED";
        testDto.setDescription(testValue);
        testDto.setPrice(50000);
        testDto.setWeight(658);
        String dtoJson = jackson.writeValueAsString(testDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/box")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto createdDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(testDto.getWeight()).isEqualTo(createdDto.getWeight());
        Truth.assertThat(testDto.getDescription()).isEqualTo(createdDto.getDescription());
        Truth.assertThat(testDto.getPrice()).isEqualTo(createdDto.getPrice());
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/" + createdDto.getUuid()))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto requestedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(testDto.getPrice()).isEqualTo(requestedDto.getPrice());
        Truth.assertThat(testDto.getWeight()).isEqualTo(requestedDto.getWeight());
        Truth.assertThat(testDto.getDescription()).isEqualTo(requestedDto.getDescription());
    }

    @Test
    void edit() throws Exception {
        BoxDto testDto = new BoxDto();
        String testValue = "EDITED";
        testDto.setDescription(testValue);
        testDto.setWeight(456);
        testDto.setPrice(78434);
        String chatDtoJson = jackson.writeValueAsString(testDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/box/%s".formatted(randomBoxUuid()))
                        .content(chatDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto editedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(testDto.getWeight()).isEqualTo(editedDto.getWeight());
        Truth.assertThat(testDto.getDescription()).isEqualTo(editedDto.getDescription());
        Truth.assertThat(testDto.getPrice()).isEqualTo(editedDto.getPrice());
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/" + editedDto.getUuid()))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto requestedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(testDto.getPrice()).isEqualTo(requestedDto.getPrice());
        Truth.assertThat(testDto.getWeight()).isEqualTo(requestedDto.getWeight());
        Truth.assertThat(testDto.getDescription()).isEqualTo(requestedDto.getDescription());
    }

    @Test
    void delete() throws Exception {
        UUID chatUuid = randomBoxUuid();
        mockMvc.perform(MockMvcRequestBuilders.delete("/box/%s".formatted(chatUuid)))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/box/%s".formatted(chatUuid)))
                .andDo(testUtils::printResponse)
                .andExpect(status().isNotFound());
    }

    private UUID randomBoxUuid() {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        return boxService.getRepository().findAllByOwner(currentUser).stream()
                .findAny().orElseThrow()
                .getUuid();
    }

}
