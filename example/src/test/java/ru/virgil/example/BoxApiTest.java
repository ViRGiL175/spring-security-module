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
import ru.virgil.example.box.BoxType;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.example.util.security.policeman.WithMockFirebasePoliceman;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.utils.AssertUtils;
import ru.virgil.utils.TestUtils;

import java.util.List;
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
    private final AssertUtils assertUtils;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box")
                        .queryParam(BoxController.PAGE_PARAM, String.valueOf(BOX_PAGE))
                        .queryParam(BoxController.PAGE_SIZE_PARAM, String.valueOf(BOX_PAGE_SIZE)))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<BoxDto> boxDtoList = testUtils.extractDtoFromResponse(mvcResult, List.class, BoxDto.class);
        Truth.assertThat(boxDtoList).isNotEmpty();
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
    void createWithoutType() throws Exception {
        BoxDto testDto = new BoxDto(null, "CREATED", 50000, 658);
        String dtoJson = jackson.writeValueAsString(testDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/box")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.USUAL, "CREATED", 50000, 658);
        String dtoJson = jackson.writeValueAsString(testDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/box")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto createdDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        assertUtils.partialEquals(createdDto, testDto);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/" + createdDto.getUuid()))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto requestedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(createdDto).isEqualTo(requestedDto);
    }

    @Test
    void edit() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.USUAL, "EDITED", 78434, 456);
        String chatDtoJson = jackson.writeValueAsString(testDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/box/%s".formatted(randomBoxUuid()))
                        .content(chatDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto editedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        assertUtils.partialEquals(editedDto, testDto);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/" + editedDto.getUuid()))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto requestedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(editedDto).isEqualTo(requestedDto);
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
        return boxService.getAll(currentUser, 0, Integer.MAX_VALUE).stream()
                .findAny().orElseThrow()
                .getUuid();
    }

    @Test
    void createWeaponByUsualUser() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.WEAPON, "CREATED-BY-USUAL-USER", 50000, 658);
        String dtoJson = jackson.writeValueAsString(testDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/box")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockFirebasePoliceman
    void createWeaponByPoliceman() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.WEAPON, "CREATED-BY-POLICEMAN", 50000, 658);
        String dtoJson = jackson.writeValueAsString(testDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/box")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto createdDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        assertUtils.partialEquals(createdDto, testDto);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/" + createdDto.getUuid()))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        BoxDto requestedDto = testUtils.extractDtoFromResponse(mvcResult, BoxDto.class);
        Truth.assertThat(createdDto).isEqualTo(requestedDto);
    }

    @Test
    void getAllWeaponsByUsualUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/box/weapons")
                        .queryParam(BoxController.PAGE_PARAM, String.valueOf(BOX_PAGE))
                        .queryParam(BoxController.PAGE_SIZE_PARAM, String.valueOf(BOX_PAGE_SIZE)))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockFirebasePoliceman
    void getAllWeaponsByPoliceman() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.WEAPON, "CREATED-BY-POLICEMAN", 50000, 658);
        String dtoJson = jackson.writeValueAsString(testDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/box")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk())
                .andReturn();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/box/weapons")
                        .queryParam(BoxController.PAGE_PARAM, String.valueOf(BOX_PAGE))
                        .queryParam(BoxController.PAGE_SIZE_PARAM, String.valueOf(BOX_PAGE_SIZE)))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<BoxDto> boxDtoList = testUtils.extractDtoFromResponse(mvcResult, List.class, BoxDto.class);
        Truth.assertThat(boxDtoList).isNotEmpty();
    }
}
