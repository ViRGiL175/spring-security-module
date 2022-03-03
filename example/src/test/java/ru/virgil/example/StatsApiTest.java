package ru.virgil.example;

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
import ru.virgil.example.stats.StatsController;
import ru.virgil.example.util.WithMockFirebaseUser;
import ru.virgil.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatsApiTest {

    private final MockMvc mockMvc;
    private final TestUtils testUtils;

    @Test
    void getAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/stats/all"))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        StatsController.StatsDto statsDto = testUtils.extractDtoFromResponse(mvcResult, StatsController.StatsDto.class);
    }


    @Test
    void getMy() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/stats/my"))
                .andDo(testUtils::printResponse)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        StatsController.StatsDto statsDto = testUtils.extractDtoFromResponse(mvcResult, StatsController.StatsDto.class);
    }

}
