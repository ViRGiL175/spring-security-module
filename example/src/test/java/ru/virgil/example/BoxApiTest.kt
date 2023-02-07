package ru.virgil.example

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.virgil.example.box.BoxController
import ru.virgil.example.box.BoxDto
import ru.virgil.example.box.BoxService
import ru.virgil.example.box.BoxType
import ru.virgil.example.user.UserDetailsService
import ru.virgil.example.util.security.policeman.WithMockFirebasePoliceman
import ru.virgil.example.util.security.user.WithMockFirebaseUser
import ru.virgil.test_utils.AssertUtils
import ru.virgil.test_utils.fluent_request.RequestUtil
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan("ru.virgil.test_utils")
class BoxApiTest @Autowired constructor(
    private val boxService: BoxService,
    private val userDetailsService: UserDetailsService,
    private val assertUtils: AssertUtils,
    private val requestUtil: RequestUtil,
) {

    @Throws(Exception::class)
    @Test
    fun getAll(): Unit {
        val boxDtoList = requestUtil["/box?%s=%s&%s=%s"
            .formatted(
                BoxController.PAGE_PARAM,
                BOX_PAGE,
                BoxController.PAGE_SIZE_PARAM,
                BOX_PAGE_SIZE
            )]
            .receive(MutableList::class.java, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as List<BoxDto?>
        Truth.assertThat(boxDtoList).isNotEmpty()
        Truth.assertThat(boxDtoList.stream().findAny().orElseThrow()).isInstanceOf(BoxDto::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        val boxDto = requestUtil["/box/%s".formatted(randomBoxUuid())]
            .receive(BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        Truth.assertThat(boxDto.weight).isAtLeast(10)
    }

    @Test
    @Throws(Exception::class)
    fun createWithoutType() {
        val testDto = BoxDto(null, "CREATED", 50000, 658f)
        requestUtil.post("/box")
            .exchange(testDto, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun create() {
        val testDto = BoxDto(BoxType.USUAL, "CREATED", 50000, 658f)
        val createdDto = requestUtil.post("/box")
            .exchange(testDto, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        assertUtils.partialEquals(createdDto, testDto)
        val serverDto = requestUtil["/box/" + createdDto.uuid]
            .receive(BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        Truth.assertThat(serverDto).isEqualTo(createdDto)
    }

    @Test
    @Throws(Exception::class)
    fun edit() {
        val testDto = BoxDto(BoxType.USUAL, "EDITED", 78434, 456f)
        val changedDto = requestUtil.put("/box/%s".formatted(randomBoxUuid()))
            .exchange(testDto, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        assertUtils.partialEquals(changedDto, testDto)
        val serverDto = requestUtil["/box/" + changedDto.uuid]
            .receive(BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        Truth.assertThat(serverDto).isEqualTo(changedDto)
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val chatUuid = randomBoxUuid()
        requestUtil.delete("/box/%s".formatted(chatUuid))
            .and()
            .expect(MockMvcResultMatchers.status().isOk)
        requestUtil["/box/%s".formatted(chatUuid)]
            .and()
            .expect(MockMvcResultMatchers.status().isNotFound)
    }

    private fun randomBoxUuid(): UUID {
        val currentUser = userDetailsService.currentUser
        return boxService.getAll(currentUser, 0, Int.MAX_VALUE).stream()
            .findAny().orElseThrow()
            .uuid
    }

    @Test
    @Throws(Exception::class)
    fun createWeaponByUsualUser() {
        val testDto = BoxDto(BoxType.WEAPON, "CREATED-BY-USUAL-USER", 50000, 658f)
        requestUtil.post("/box")
            .send(testDto)
            .and()
            .expect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockFirebasePoliceman
    @Throws(Exception::class)
    fun createWeaponByPoliceman() {
        val testDto = BoxDto(BoxType.WEAPON, "CREATED-BY-POLICEMAN", 50000, 658f)
        val createdDto = requestUtil.post("/box")
            .exchange(testDto, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        assertUtils.partialEquals(createdDto, testDto)
        val serverDto = requestUtil["/box/%s".formatted(createdDto.uuid)]
            .receive(BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        Truth.assertThat(createdDto).isEqualTo(serverDto)
    }

    @Throws(Exception::class)
    @Test
    fun getAllWeaponsByUsualUser(): Unit {
        requestUtil["/box/weapons?%s=%s&%s=%s"
            .formatted(
                BoxController.PAGE_PARAM,
                BOX_PAGE,
                BoxController.PAGE_SIZE_PARAM,
                BOX_PAGE_SIZE
            )]
            .and()
            .expect(MockMvcResultMatchers.status().isForbidden)
    }

    @Throws(Exception::class)
    @WithMockFirebasePoliceman
    @Test
    fun getAllWeaponsByPoliceman(): Unit {
        val testDto = BoxDto(BoxType.WEAPON, "CREATED-BY-POLICEMAN", 50000, 658f)
        val serverDto = requestUtil.post("/box")
            .exchange(testDto, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as BoxDto
        assertUtils.partialEquals(serverDto, testDto)
        val weaponDtoList = requestUtil["/box/weapons?%s=%s&%s=%s"
            .formatted(
                BoxController.PAGE_PARAM,
                BOX_PAGE,
                BoxController.PAGE_SIZE_PARAM,
                BOX_PAGE_SIZE
            )]
            .receive(MutableList::class.java, BoxDto::class.java)
            .and()
            .expect(MockMvcResultMatchers.status().isOk) as List<BoxDto?>
        Truth.assertThat(weaponDtoList).contains(serverDto)
    }

    companion object {

        const val BOX_PAGE = 0
        const val BOX_PAGE_SIZE = 10
    }
}
