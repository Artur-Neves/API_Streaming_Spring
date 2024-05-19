package br.com.StreamChallenge.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SpringConfiguration {
    @Bean
    SecurityFilterChain configuration (HttpSecurity http) throws Exception {
        return http.csrf(csrf-> csrf.disable())
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req ->{
                    req.requestMatchers(HttpMethod.GET, "/videos/**", "/category/**").permitAll();
                    req.anyRequest().hasAuthority("ADMIN");

                })


                .build();
    }
}
