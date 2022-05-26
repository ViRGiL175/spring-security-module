package ru.virgil.example;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.virgil.example.box.BoxController;
import ru.virgil.example.order.BuyingOrderDto;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.truck.TruckDto;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.utils.TestUtils;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuyingOrderApiTest {

    public static final int PAGE = 0;
    public static final int PAGE_SIZE = 10;
    private final MockMvc mockMvc;
    private final BuyingOrderService buyingOrderService;
    private final UserDetailsService userDetailsService;
    private final TestUtils testUtils;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/buying_order")
                        .queryParam(BoxController.PAGE_PARAM, String.valueOf(PAGE))
                        .queryParam(BoxController.PAGE_SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<BuyingOrderDto> buyingOrderDtoList = testUtils
                .extractDtoFromResponse(mvcResult, List.class, BuyingOrderDto.class);
        Truth.assertThat(buyingOrderDtoList).isNotEmpty();
    }

    @Test
    void get() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/buying_order/%s"
                        .formatted(randomOrderUuid())))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        BuyingOrderDto dto = testUtils.extractDtoFromResponse(mvcResult, BuyingOrderDto.class);
        Truth.assertThat(dto.getDescription()).isNotEmpty();
    }

    @Test
    void getTruckByOrder() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/buying_order/%s/truck"
                                .formatted(randomOrderUuid()))
                        .queryParam(BoxController.PAGE_PARAM, String.valueOf(PAGE))
                        .queryParam(BoxController.PAGE_SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<TruckDto> truckDtoList = testUtils
                .extractDtoFromResponse(mvcResult, List.class, TruckDto.class);
        Truth.assertThat(truckDtoList).isNotEmpty();
    }

    private UUID randomOrderUuid() {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        return buyingOrderService.getAll(currentUser, PAGE, PAGE_SIZE).stream()
                .findAny().orElseThrow()
                .getUuid();
    }
}
