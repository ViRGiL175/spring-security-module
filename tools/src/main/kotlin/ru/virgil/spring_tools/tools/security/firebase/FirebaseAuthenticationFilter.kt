package ru.virgil.spring_tools.tools.security.firebase

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.truth.Truth
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import ru.virgil.spring_tools.tools.security.SecurityConfig
import ru.virgil.spring_tools.tools.security.SecurityProperties
import ru.virgil.spring_tools.tools.security.entity.AuthMethods
import ru.virgil.spring_tools.tools.security.header.AuthorizationHeader
import java.io.IOException


class FirebaseAuthenticationFilter(
    securityProperties: SecurityProperties,
    private val serializer: ObjectMapper,
    private val log: Logger = LoggerFactory.getLogger(this::class.java),
) : AbstractAuthenticationProcessingFilter(SecurityConfig.AUTH_API_PATHS[AuthMethods.FIREBASE]) {

    init {
        if (securityProperties.disablePostAuthRedirect) {
            setAuthenticationSuccessHandler(NoRedirectsHandler())
        }
    }

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val authorizationHeader: AuthorizationHeader
        try {
            Truth.assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isNotNull()
            Truth.assertThat(request.getHeader(HttpHeaders.AUTHORIZATION)).isNotEmpty()
            val rawAuthorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            authorizationHeader = serializer.readValue(rawAuthorizationHeader, AuthorizationHeader::class.java)
            Truth.assertThat(authorizationHeader.authScheme).isEqualTo(AuthMethods.FIREBASE)
            Truth.assertThat(authorizationHeader.credentials).containsKey(FIREBASE_UID_CREDENTIAL)
            Truth.assertThat(authorizationHeader.credentials).containsKey(FIREBASE_AUTH_TOKEN_CREDENTIAL)
        } catch (exception: Throwable) {
            when (exception) {
                is AssertionError, is JacksonException -> {
                    val headerExample = serializer.writeValueAsString(AUTHORIZATION_HEADER_EXAMPLE)
                    throw FirebaseAuthorizationException(
                        "Header ${HttpHeaders.AUTHORIZATION} should be like $headerExample", exception
                    )
                }

                else -> throw exception
            }
        }
        val firebaseAuthenticationToken = FirebaseAuthenticationToken(
            DEFAULT_AUTHORITIES,
            authorizationHeader.credentials[FIREBASE_UID_CREDENTIAL]!!,
            authorizationHeader.credentials[FIREBASE_AUTH_TOKEN_CREDENTIAL]!!
        )
        return authenticationManager.authenticate(firebaseAuthenticationToken)
    }

    inner class NoRedirectsHandler : AuthenticationSuccessHandler {

        override fun onAuthenticationSuccess(
            request: HttpServletRequest, response: HttpServletResponse,
            authentication: Authentication,
        ) {
            log.info("Auth Redirects is disabled in %s".format(SecurityProperties::class.java.simpleName))
        }
    }

    companion object {

        const val FIREBASE_UID_CREDENTIAL = "firebase_uid"
        const val FIREBASE_AUTH_TOKEN_CREDENTIAL = "firebase_auth_token"
        val AUTHORIZATION_HEADER_EXAMPLE = AuthorizationHeader(
            AuthMethods.FIREBASE,
            mapOf(Pair(FIREBASE_UID_CREDENTIAL, "value"), Pair(FIREBASE_AUTH_TOKEN_CREDENTIAL, "value"))
        )
        val DEFAULT_AUTHORITIES = listOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_USER"))
    }
}
