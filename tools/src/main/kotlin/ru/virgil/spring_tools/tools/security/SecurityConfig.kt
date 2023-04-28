package ru.virgil.spring_tools.tools.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.virgil.spring_tools.tools.security.entity.AuthMethods
import ru.virgil.spring_tools.tools.security.firebase.FirebaseAuthenticationFilter
import ru.virgil.spring_tools.tools.security.firebase.FirebaseAuthorizationProvider

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
open class SecurityConfig(
    private val firebaseAuthorizationProvider: FirebaseAuthorizationProvider,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val securityProperties: SecurityProperties,
    private val objectMapper: ObjectMapper,
) {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain = setupFilterChain(httpSecurity).build()

    protected open fun setupFilterChain(httpSecurity: HttpSecurity): HttpSecurity {
        val propertyIgnoredPaths: List<String> = securityProperties.anonymousPaths
        val security = httpSecurity
            // todo: стандартный редирект на страницу успешной безопасности
            // todo: разобраться, как включить
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(*AUTH_PAGE_PATHS.values.toTypedArray()).permitAll()
            .requestMatchers("/", "/favicon.ico", "/error").permitAll()
            .requestMatchers(*propertyIgnoredPaths.toTypedArray()).permitAll()
            .requestMatchers("/**").authenticated()
            .and()
            .addFilterBefore(
                FirebaseAuthenticationFilter(securityProperties, objectMapper),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authenticationProvider(firebaseAuthorizationProvider)
        // todo: разобраться, как лучше реагировать на ошибки и не подставлять безопасность
        // .exceptionHandling()
        // .authenticationEntryPoint(authenticationEntryPoint)
        return security
    }

    companion object {

        val AUTH_API_PATHS = mapOf(Pair(AuthMethods.FIREBASE, "/auth/firebase"))

        val AUTH_PAGE_PATHS = mapOf(Pair(AuthMethods.FIREBASE, "/auth/firebase/page/**"))
    }

}
