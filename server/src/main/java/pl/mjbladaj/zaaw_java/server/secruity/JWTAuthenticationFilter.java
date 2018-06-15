package pl.mjbladaj.zaaw_java.server.secruity;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.core.Authentication;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        try {
            Authentication authentication = TokenAuthenticationUtils
                    .getAuthentication((HttpServletRequest)request,
                            (HttpServletResponse) response);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request,response);
        } catch (ExpiredJwtException e) {
            HttpServletResponse httpResponse =(HttpServletResponse) response;

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Token timed out. Login again.");
        }

    }
}