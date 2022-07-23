package ru.virgil.security.firebase;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.virgil.security.SecurityConfig;
import ru.virgil.security.entity.AuthMethods;
import ru.virgil.security.header.AuthorizationHeader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;


public class FirebaseAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String FIREBASE_UID_CREDENTIAL = "firebase_uid";
    public static final String FIREBASE_AUTH_TOKEN_CREDENTIAL = "firebase_auth_token";
    public static final AuthorizationHeader AUTHORIZATION_HEADER_EXAMPLE = new AuthorizationHeader(AuthMethods.FIREBASE,
            Map.of(FIREBASE_UID_CREDENTIAL, "value", FIREBASE_AUTH_TOKEN_CREDENTIAL, "value"));
    public static final List<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    // todo: перейти на сериализацию CSV или похожую
    private final ObjectMapper serializer = new ObjectMapper();

    public FirebaseAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(SecurityConfig.AUTH_API_PATHS.get(AuthMethods.FIREBASE), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        AuthorizationHeader authorizationHeader;
        try {
            assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isNotNull();
            assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isNotEmpty();
            String rawAuthorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            authorizationHeader = serializer.readValue(rawAuthorizationHeader, AuthorizationHeader.class);
            assertThat(authorizationHeader.authScheme()).isEqualTo(AuthMethods.FIREBASE);
            assertThat(authorizationHeader.credentials()).containsKey(FIREBASE_UID_CREDENTIAL);
            assertThat(authorizationHeader.credentials()).containsKey(FIREBASE_AUTH_TOKEN_CREDENTIAL);
        } catch (AssertionError | JacksonException exception) {
            String headerExample = serializer.writeValueAsString(AUTHORIZATION_HEADER_EXAMPLE);
            throw new FirebaseAuthorizationException("Header %s should be like %s".formatted(HttpHeaders.AUTHORIZATION,
                    headerExample), exception);
        }
        FirebaseAuthenticationToken firebaseAuthenticationToken = new FirebaseAuthenticationToken(DEFAULT_AUTHORITIES,
                authorizationHeader.credentials().get(FIREBASE_UID_CREDENTIAL),
                authorizationHeader.credentials().get(FIREBASE_AUTH_TOKEN_CREDENTIAL));
        return this.getAuthenticationManager().authenticate(firebaseAuthenticationToken);
    }
}
