package ru.virgil.example;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.virgil.example.box.BoxController;
import ru.virgil.example.box.BoxDto;
import ru.virgil.example.box.BoxService;
import ru.virgil.example.box.BoxType;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.example.util.security.policeman.WithMockFirebasePoliceman;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.test_utils.AssertUtils;
import ru.virgil.test_utils.fluent_request.RequestUtil;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan("ru.virgil.test_utils")
public class BoxApiTest {

    public static final int BOX_PAGE = 0;
    public static final int BOX_PAGE_SIZE = 10;
    private final BoxService boxService;
    private final UserDetailsService userDetailsService;
    private final AssertUtils assertUtils;
    private final RequestUtil requestUtil;

    @Test
    void getAll() throws Exception {
        List<BoxDto> boxDtoList = (List<BoxDto>) requestUtil.get("/box?%s=%s&%s=%s"
                        .formatted(BoxController.PAGE_PARAM, BOX_PAGE, BoxController.PAGE_SIZE_PARAM, BOX_PAGE_SIZE))
                .receive(List.class, BoxDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(boxDtoList).isNotEmpty();
        Truth.assertThat(boxDtoList.stream().findAny().orElseThrow()).isInstanceOf(BoxDto.class);
    }

    @Test
    void get() throws Exception {
        BoxDto boxDto = (BoxDto) requestUtil.get("/box/%s".formatted(randomBoxUuid()))
                .receive(BoxDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(boxDto.getWeight()).isAtLeast(10);
    }

    @Test
    void createWithoutType() throws Exception {
        BoxDto testDto = new BoxDto(null, "CREATED", 50000, 658);
        requestUtil.post("/box")
                .exchange(testDto, BoxDto.class)
                .and()
                .expect(status().isBadRequest());
    }

    @Test
    void create() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.USUAL, "CREATED", 50000, 658);
        BoxDto createdDto = (BoxDto) requestUtil.post("/box")
                .exchange(testDto, BoxDto.class)
                .and()
                .expect(status().isOk());
        assertUtils.partialEquals(createdDto, testDto);
        BoxDto serverDto = (BoxDto) requestUtil.get("/box/" + createdDto.getUuid())
                .receive(BoxDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(serverDto).isEqualTo(createdDto);
    }

    @Test
    void edit() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.USUAL, "EDITED", 78434, 456);
        BoxDto changedDto = (BoxDto) requestUtil.put("/box/%s".formatted(randomBoxUuid()))
                .exchange(testDto, BoxDto.class)
                .and()
                .expect(status().isOk());
        assertUtils.partialEquals(changedDto, testDto);
        BoxDto serverDto = (BoxDto) requestUtil.get("/box/" + changedDto.getUuid())
                .receive(BoxDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(serverDto).isEqualTo(changedDto);
    }

    @Test
    void delete() throws Exception {
        UUID chatUuid = randomBoxUuid();
        requestUtil.delete("/box/%s".formatted(chatUuid))
                .and()
                .expect(status().isOk());
        requestUtil.get("/box/%s".formatted(chatUuid))
                .and()
                .expect(status().isNotFound());
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
        requestUtil.post("/box")
                .send(testDto)
                .and()
                .expect(status().isForbidden());
    }

    @Test
    @WithMockFirebasePoliceman
    void createWeaponByPoliceman() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.WEAPON, "CREATED-BY-POLICEMAN", 50000, 658);
        BoxDto createdDto = (BoxDto) requestUtil.post("/box")
                .exchange(testDto, BoxDto.class)
                .and()
                .expect(status().isOk());
        assertUtils.partialEquals(createdDto, testDto);
        BoxDto serverDto = (BoxDto) requestUtil.get("/box/%s".formatted(createdDto.getUuid()))
                .receive(BoxDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(createdDto).isEqualTo(serverDto);
    }

    @Test
    void getAllWeaponsByUsualUser() throws Exception {
        requestUtil.get("/box/weapons?%s=%s&%s=%s"
                        .formatted(BoxController.PAGE_PARAM, BOX_PAGE, BoxController.PAGE_SIZE_PARAM, BOX_PAGE_SIZE))
                .and()
                .expect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockFirebasePoliceman
    void getAllWeaponsByPoliceman() throws Exception {
        BoxDto testDto = new BoxDto(BoxType.WEAPON, "CREATED-BY-POLICEMAN", 50000, 658);
        BoxDto serverDto = (BoxDto) requestUtil.post("/box")
                .exchange(testDto, BoxDto.class)
                .and()
                .expect(status().isOk());
        assertUtils.partialEquals(serverDto, testDto);
        List<BoxDto> weaponDtoList = (List<BoxDto>) requestUtil.get("/box/weapons?%s=%s&%s=%s"
                        .formatted(BoxController.PAGE_PARAM, BOX_PAGE, BoxController.PAGE_SIZE_PARAM, BOX_PAGE_SIZE))
                .receive(List.class, BoxDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(weaponDtoList).contains(serverDto);
    }
}
