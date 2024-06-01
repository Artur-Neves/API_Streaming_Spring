package br.com.StreamChallenge.infra.filters;

import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import br.com.StreamChallenge.infra.token.TokenService;
import br.com.StreamChallenge.service.UserService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilterSystem extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;


    @Override

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        if (token!=null) {
            String subjct = tokenService.verify(token);
            UserDetails user = userService.loadUserByUsername(subjct);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        doFilter(request, response, filterChain);

    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token==null || token.isEmpty()) {
            return null;
        }
        return (token.contains("Bearer ") ? token.replace("Bearer ", "") : token);
    }
}
