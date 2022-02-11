package ru.virgil.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.virgil.security.entity.AuthMethods;
import ru.virgil.security.firebase.FirebaseAuthenticationFilter;
import ru.virgil.security.header.WwwAuthenticateHeader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ObjectMapper jackson;

    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        addFirebaseAuthChallenges(response);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
    }

    public void addFirebaseAuthChallenges(HttpServletResponse response) throws JsonProcessingException {
        String challengeInfo = "%s Header is needed for %s".formatted(HttpHeaders.AUTHORIZATION,
                SecurityConfig.AUTH_API_PATHS.get(AuthMethods.FIREBASE));
        String challengeFormat = jackson.writeValueAsString(FirebaseAuthenticationFilter.AUTHORIZATION_HEADER_EXAMPLE);
        WwwAuthenticateHeader wwwAuthenticateHeader = new WwwAuthenticateHeader(AuthMethods.FIREBASE,
                Map.of("info", challengeInfo, "format", challengeFormat));
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, jackson.writeValueAsString(wwwAuthenticateHeader));
    }
}
