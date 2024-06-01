package br.com.StreamChallenge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ADMIN ( "ADMIN"),
    USER( "USER");
    private String desc;

    Roles(String desc) {
        this.desc= desc;
    }


    @Override
    public String getAuthority() {
        return desc;
    }
}
