package br.com.StreamChallenge.dto.User;

import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserUpdateDto(
        @NotBlank
        String password,
        @NotNull
        Set<Roles> roles
) {
    public UserUpdateDto(User user) {
        this( user.getPassword(), user.getRoles());
    }
}
