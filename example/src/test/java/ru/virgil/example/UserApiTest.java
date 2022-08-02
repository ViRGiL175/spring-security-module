package ru.virgil.example;

import com.google.common.truth.Truth;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.virgil.example.user.UserDetailsDto;
import ru.virgil.example.util.security.user.WithMockFirebaseUser;
import ru.virgil.testutils.fluent_request.RequestUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockFirebaseUser
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiTest {

    private final RequestUtil requestUtil;

    @Test
    void get() throws Exception {
        UserDetailsDto userDetailsDto = (UserDetailsDto) requestUtil.get("/user_details")
                .receive(UserDetailsDto.class)
                .and()
                .expect(status().isOk());
        Truth.assertThat(userDetailsDto).isNotNull();
    }
}
