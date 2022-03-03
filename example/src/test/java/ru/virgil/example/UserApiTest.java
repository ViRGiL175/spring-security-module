package ru.virgil.example;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.virgil.example.util.WithMockFirebaseUser;
import ru.virgil.utils.TestUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiTest {

    private final MockMvc mockMvc;
    private final TestUtils testUtils;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user_details"))
                .andDo(testUtils::printResponse)
                .andExpect(status().isOk());
    }
}
