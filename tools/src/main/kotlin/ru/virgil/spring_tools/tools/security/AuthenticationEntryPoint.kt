package ru.virgil.spring_tools.tools.security

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import ru.virgil.spring_tools.tools.security.entity.AuthMethods
import ru.virgil.spring_tools.tools.security.firebase.FirebaseAuthenticationFilter
import ru.virgil.spring_tools.tools.security.header.WwwAuthenticateHeader
import java.io.IOException


@Component
class AuthenticationEntryPoint(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    /**
     * {@inheritDoc}
     */
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        addFirebaseAuthChallenges(response)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.localizedMessage)
    }

    @Throws(JsonProcessingException::class)
    fun addFirebaseAuthChallenges(response: HttpServletResponse) {
        val challengeInfo = "%s Header is needed for %s".format(
            HttpHeaders.AUTHORIZATION,
            SecurityConfig.AUTH_API_PATHS[AuthMethods.FIREBASE]
        )
        val challengeFormat = objectMapper.writeValueAsString(FirebaseAuthenticationFilter.AUTHORIZATION_HEADER_EXAMPLE)
        val wwwAuthenticateHeader = WwwAuthenticateHeader(
            AuthMethods.FIREBASE,
            mapOf(Pair("info", challengeInfo), Pair("format", challengeFormat))
        )
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, objectMapper.writeValueAsString(wwwAuthenticateHeader))
    }
}
