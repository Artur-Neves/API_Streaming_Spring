package br.com.StreamChallenge.infra.token;

import br.com.StreamChallenge.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Service
public class TokenService {
    @Value("${api.security.secretSecurity}")
    private String secretKey;

    public String createdToken(User user){
    try {
        Algorithm ALGORITHM = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("VideosAPI")
                .withSubject(user.getUsername())
                .withExpiresAt(timeExpires())
                .sign(ALGORITHM);

    } catch (JWTCreationException exception){
        throw new RuntimeException("Failure generated token");
    }
    }

    private Instant timeExpires() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String verify(String token) {
        try {
            Algorithm ALGORITHM = Algorithm.HMAC256(secretKey);
            return JWT.require(ALGORITHM)
                    // specify any specific claim validations
                    .withIssuer("VideosAPI")
                    // reusable verifier instance
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalid or expired");
        }
    }
}
