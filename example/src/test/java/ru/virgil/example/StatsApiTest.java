package ru.virgil.example;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.virgil.example.stats.StatsDto;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.utils.fluent_request.RequestUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatsApiTest {

    private final RequestUtil requestUtil;

    @Test
    void getAll() throws Exception {
        StatsDto statsDto = (StatsDto) requestUtil.get("/stats/all")
                .receive(StatsDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(statsDto).isNotNull();
    }

    @Test
    void getMy() throws Exception {
        StatsDto statsDto = (StatsDto) requestUtil.get("/stats/my")
                .receive(StatsDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(statsDto).isNotNull();
    }

}
