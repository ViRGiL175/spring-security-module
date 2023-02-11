package ru.virgil.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.virgil.security.entity.AuthMethods
import ru.virgil.security.firebase.FirebaseAuthenticationFilter
import ru.virgil.security.firebase.FirebaseAuthorizationProvider
import java.util.Map


@Configuration
class SecurityConfig(
    private val firebaseAuthorizationProvider: FirebaseAuthorizationProvider,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val securityProperties: SecurityProperties,
    private val objectMapper: ObjectMapper,
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        val propertyIgnoredPaths: List<String> = securityProperties.anonymousPaths
        httpSecurity // todo: стандартный редирект на страницу успешной безопасности
            // todo: разобраться, как включить
            .csrf().disable()
            .authorizeRequests()
            .mvcMatchers(*AUTH_PAGE_PATHS.values.toTypedArray()).permitAll()
            .mvcMatchers("/", "/favicon.ico", "/error").permitAll()
            .mvcMatchers(*propertyIgnoredPaths.toTypedArray()).permitAll()
            .mvcMatchers("/**").authenticated()
            .and()
            .addFilterBefore(
                FirebaseAuthenticationFilter(authenticationManager(), securityProperties, objectMapper),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authenticationProvider(firebaseAuthorizationProvider) // todo: разобраться, как лучше реагировать на ошибки и не подставлять безопасность
        // .exceptionHandling()
        // .authenticationEntryPoint(authenticationEntryPoint)
    }

    companion object {

        @JvmField
        val AUTH_API_PATHS = Map.of(
            AuthMethods.FIREBASE, "/auth/firebase"
        )

        @JvmField
        val AUTH_PAGE_PATHS = Map.of(
            AuthMethods.FIREBASE, "/auth/firebase/page/**"
        )
    }
}
