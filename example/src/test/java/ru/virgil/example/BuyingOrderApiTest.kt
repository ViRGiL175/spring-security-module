package ru.virgil.example

import com.google.common.truth.Truth
import org.apache.http.client.utils.URIBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.virgil.example.box.BoxController
import ru.virgil.example.order.BuyingOrderDto
import ru.virgil.example.order.BuyingOrderService
import ru.virgil.example.truck.TruckDto
import ru.virgil.example.user.UserDetailsService
import ru.virgil.example.util.security.user.WithMockFirebaseUser
import ru.virgil.test_utils.fluent_request.RequestUtil
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan("ru.virgil.test_utils")
class BuyingOrderApiTest @Autowired constructor(
    private val buyingOrderService: BuyingOrderService,
    private val userDetailsService: UserDetailsService,
    private val requestUtil: RequestUtil,
) {

    @Throws(Exception::class)
    @Test
    fun getAll(): Unit {
        val uriBuilder = URIBuilder().setPath("/buying_order")
            .addParameter(BoxController.PAGE_PARAM, PAGE.toString())
            .addParameter(BoxController.PAGE_SIZE_PARAM, PAGE_SIZE.toString())
        val buyingOrderDtoList = requestUtil[uriBuilder.toString()]
            .receive(MutableList::class.java, BuyingOrderDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as List<BuyingOrderDto>
        Truth.assertThat(buyingOrderDtoList.stream().findAny().orElseThrow())
            .isInstanceOf(BuyingOrderDto::class.java)

        // todo: сначала обращаемся напрямую к серверу, а потом делаем запрос, не одновременно
        // todo: почему не проходит проверка?
        //        BuyingOrderDto randomBuyingOrderDto = requestUtil.get("/buying_order/%s".formatted(randomOrderUuid()))
        //                .receive(BuyingOrderDto.class)
        //                .expect(status().isOk());
        //        Truth.assertThat(buyingOrderDtoList).contains(randomBuyingOrderDto);
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        val randomBuyingOrderDto = requestUtil["/buying_order/%s".formatted(randomOrderUuid())]
            .receive(BuyingOrderDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BuyingOrderDto
        Truth.assertThat(randomBuyingOrderDto.description).isNotEmpty()
    }

    @Throws(Exception::class)
    @Test
    fun getTruckByOrder(): Unit {
        val uri = URIBuilder().setPathSegments("buying_order", randomOrderUuid().toString(), "truck")
            .addParameter(BoxController.PAGE_PARAM, PAGE.toString())
            .addParameter(BoxController.PAGE_SIZE_PARAM, PAGE_SIZE.toString())
            .toString()
        val truckDtoList = requestUtil[uri]
            .receive(MutableList::class.java, TruckDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as List<TruckDto>
        Truth.assertThat(truckDtoList.stream().findAny().orElseThrow()).isInstanceOf(TruckDto::class.java)
    }

    private fun randomOrderUuid(): UUID {
        val currentUser = userDetailsService.currentUser
        return buyingOrderService.getAll(currentUser, PAGE, PAGE_SIZE).stream()
            .findAny().orElseThrow()
            .uuid
    }

    companion object {

        const val PAGE = 0
        const val PAGE_SIZE = 10
    }
}
