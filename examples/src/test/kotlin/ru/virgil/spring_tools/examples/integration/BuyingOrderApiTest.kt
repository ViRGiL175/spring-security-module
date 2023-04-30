package ru.virgil.spring_tools.examples.integration

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldNotBeEmpty
import org.apache.http.client.utils.URIBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring_tools.examples.order.BuyingOrder
import ru.virgil.spring_tools.examples.order.BuyingOrderDto
import ru.virgil.spring_tools.examples.order.BuyingOrderMocker
import ru.virgil.spring_tools.examples.system.rest.RestValues
import ru.virgil.spring_tools.examples.truck.TruckDto
import ru.virgil.spring_tools.examples.user.WithMockFirebaseUser
import ru.virgil.spring_tools.tools.testing.fluent.Fluent

@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuyingOrderApiTest @Autowired constructor(
    val fluent: Fluent,
    @Qualifier(BuyingOrderMocker.random)
    val randomBuyingOrderProvider: ObjectProvider<BuyingOrder>,
) {

    private val page = 0
    private val pageSize = 10

    @Test
    fun getAll() {
        val buyingOrderDtoList: List<BuyingOrderDto> = fluent.request {
            get { "/buying_order?${RestValues.pageParam}=$page&${RestValues.pageSizeParam}=$pageSize" }
        }
        buyingOrderDtoList.shouldNotBeEmpty()
    }

    @Test
    fun get() {
        val buyingOrder = randomBuyingOrderProvider.getObject()
        val randomBuyingOrderDto: BuyingOrderDto = fluent.request { get { "/buying_order/${buyingOrder.uuid}" } }
        randomBuyingOrderDto.description!!.shouldNotBeEmpty()
    }

    @Test
    fun getTruckByOrder() {
        val buyingOrder = randomBuyingOrderProvider.getObject()
        val uri = URIBuilder().setPathSegments("buying_order", buyingOrder.uuid.toString(), "truck")
            .addParameter(RestValues.pageParam, page.toString())
            .addParameter(RestValues.pageSizeParam, pageSize.toString())
            .toString()
        val truckDtoList: List<TruckDto> = fluent.request { get { uri } }
        truckDtoList.shouldNotBeEmpty()
    }

}
