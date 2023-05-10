package ru.virgil.security.cors;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
// todo: порядок не важен?
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

    private final CorsProperties corsProperties;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!corsProperties.isUseCustomCors()) {
            chain.doFilter(req, res);
        } else {
            // todo: внедрение из свойств?
            HttpServletResponse servletResponse = (HttpServletResponse) res;
            setHeaders(servletResponse, "Access-Control-Allow-Origin",
                    corsProperties.getAllowOrigin());
            setHeaders(servletResponse, "Access-Control-Allow-Methods",
                    Arrays.stream(corsProperties.getAllowMethods()).map(Enum::name).collect(Collectors.joining(", ")));
            setHeaders(servletResponse, "Access-Control-Max-Age",
                    String.valueOf(corsProperties.getMaxAge()));
            setHeaders(servletResponse, "Access-Control-Allow-Headers",
                    String.join(" ,", corsProperties.getAllowHeaders()));
            setHeaders(servletResponse, "Access-Control-Expose-Headers",
                    String.join(" ,", corsProperties.getExposedHeaders()));
            setHeaders(servletResponse, "Access-Control-Allow-Credentials",
                    String.valueOf(corsProperties.isAllowCredential()));
            chain.doFilter(req, res);
        }
    }

    private void setHeaders(HttpServletResponse servletResponse, String header, @Nullable String value) {
        Optional.ofNullable(value).ifPresent(s -> servletResponse.setHeader(header, s));
    }

}
