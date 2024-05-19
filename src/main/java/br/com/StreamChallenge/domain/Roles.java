package br.com.StreamChallenge.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ADMIN ("ADIMIN"),
            USER("USER");
    private String desc;

    Roles(String desc) {
        this.desc= desc;
    }


    @Override
    public String getAuthority() {
        return desc;
    }
}
