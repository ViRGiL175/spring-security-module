package ru.virgil.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.virgil.security.entity.AuthMethods;
import ru.virgil.security.firebase.FirebaseAuthenticationFilter;
import ru.virgil.security.firebase.FirebaseAuthorizationProvider;

import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final Map<AuthMethods, String> AUTH_API_PATHS = Map.of(
            AuthMethods.FIREBASE, "/auth/firebase"
    );
    public static final Map<AuthMethods, String> AUTH_PAGE_PATHS = Map.of(
            AuthMethods.FIREBASE, "/auth/firebase/page/**"
    );
    private final FirebaseAuthorizationProvider firebaseAuthorizationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityProperties securityProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String[] propertyIgnoredPaths = securityProperties.getAnonymousPaths();
        httpSecurity
                // todo: стандартный редирект на страницу успешной безопасности
                // todo: разобраться, как включить
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers(AUTH_PAGE_PATHS.values().toArray(String[]::new)).permitAll()
                .mvcMatchers("/", "/favicon.ico", "/error").permitAll()
                .mvcMatchers(propertyIgnoredPaths).permitAll()
                .mvcMatchers("/**").authenticated()
                .and()
                .addFilterBefore(
                        new FirebaseAuthenticationFilter(authenticationManager(), securityProperties, objectMapper),
                        UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(firebaseAuthorizationProvider)
        // todo: разобраться, как лучше реагировать на ошибки и не подставлять безопасность
        // .exceptionHandling()
        // .authenticationEntryPoint(authenticationEntryPoint)
        ;
    }
}
