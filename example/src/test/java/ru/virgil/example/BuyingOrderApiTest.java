package ru.virgil.example;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.virgil.example.box.BoxController;
import ru.virgil.example.order.BuyingOrderDto;
import ru.virgil.example.order.BuyingOrderService;
import ru.virgil.example.truck.TruckDto;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
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
public class BuyingOrderApiTest {

    public static final int PAGE = 0;
    public static final int PAGE_SIZE = 10;
    private final BuyingOrderService buyingOrderService;
    private final UserDetailsService userDetailsService;
    private final RequestUtil requestUtil;

    @Test
    void getAll() throws Exception {
        URIBuilder uriBuilder = new URIBuilder().setPath("/buying_order")
                .addParameter(BoxController.PAGE_PARAM, String.valueOf(PAGE))
                .addParameter(BoxController.PAGE_SIZE_PARAM, String.valueOf(PAGE_SIZE));
        List<BuyingOrderDto> buyingOrderDtoList = (List<BuyingOrderDto>) requestUtil.get(uriBuilder.toString())
                .receive(List.class, BuyingOrderDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(buyingOrderDtoList.stream().findAny().orElseThrow()).isInstanceOf(BuyingOrderDto.class);

        // todo: сначала обращаемся напрямую к серверу, а потом делаем запрос, не одновременно
        // todo: почему не проходит проверка?
        //        BuyingOrderDto randomBuyingOrderDto = requestUtil.get("/buying_order/%s".formatted(randomOrderUuid()))
        //                .receive(BuyingOrderDto.class)
        //                .expect(status().isOk());
        //        Truth.assertThat(buyingOrderDtoList).contains(randomBuyingOrderDto);

    }

    @Test
    void get() throws Exception {
        BuyingOrderDto randomBuyingOrderDto = (BuyingOrderDto) requestUtil.get("/buying_order/%s".formatted(randomOrderUuid()))
                .receive(BuyingOrderDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(randomBuyingOrderDto.getDescription()).isNotEmpty();
    }

    @Test
    void getTruckByOrder() throws Exception {
        String uri = new URIBuilder().setPathSegments("buying_order", randomOrderUuid().toString(), "truck")
                .addParameter(BoxController.PAGE_PARAM, String.valueOf(PAGE))
                .addParameter(BoxController.PAGE_SIZE_PARAM, String.valueOf(PAGE_SIZE))
                .toString();
        List<TruckDto> truckDtoList = (List<TruckDto>) requestUtil.get(uri)
                .receive(List.class, TruckDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(truckDtoList.stream().findAny().orElseThrow()).isInstanceOf(TruckDto.class);
    }

    private UUID randomOrderUuid() {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        return buyingOrderService.getAll(currentUser, PAGE, PAGE_SIZE).stream()
                .findAny().orElseThrow()
                .getUuid();
    }
}
