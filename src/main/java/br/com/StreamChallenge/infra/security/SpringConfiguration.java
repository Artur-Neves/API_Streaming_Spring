package br.com.StreamChallenge.infra.security;

import br.com.StreamChallenge.infra.filters.AuthenticationFilterSystem;
import br.com.StreamChallenge.infra.filters.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import  org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@EnableMethodSecurity
public class SpringConfiguration {
    @Autowired
    private AuthenticationFilterSystem authenticationFilter;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    SecurityFilterChain configuration (HttpSecurity http) throws Exception {

        return http.csrf(csrf-> csrf.disable())
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req ->{
                    req.requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/user/**", "/user").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/videos/**", "/category/**").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/user/**").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/user/**").authenticated();
                    req.anyRequest().hasAuthority("ADMIN");
                })
              .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex->{
                    ex.authenticationEntryPoint(customAuthenticationEntryPoint);
                })
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

}
