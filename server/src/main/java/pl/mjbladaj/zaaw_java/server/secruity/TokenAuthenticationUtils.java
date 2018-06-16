package pl.mjbladaj.zaaw_java.server.secruity;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class TokenAuthenticationUtils {
    static final String SECRET = "InMySecretLife";
    static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";

    public static String getAuthoritiesSeparatedByComa(Collection<? extends GrantedAuthority> grantedAuthorities) {
        String notSeparated = grantedAuthorities.toString();
        return notSeparated.substring(1, notSeparated.length() - 1);
    }

    public static String getUserLogin(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(TokenAuthenticationUtils.SECRET)
                .parseClaimsJws(token.replace(TokenAuthenticationUtils.TOKEN_PREFIX, ""))
                .getBody();

        return claims.getSubject();
    }

    public static String buildToken(String name) {
        return Jwts.builder()
                .setSubject(name)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static void addAuthentication(HttpServletResponse res, Authentication auth) throws IOException {
        String JWT = buildToken(auth.getName());
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }
    public static String getUser(Claims claims) {
        return claims.getSubject();
    }


    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            String user = getUser(claims);
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, null) :
                    null;
        }
        return null;
    }
}



