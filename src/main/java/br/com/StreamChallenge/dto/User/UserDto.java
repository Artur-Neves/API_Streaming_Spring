package br.com.StreamChallenge.dto.User;

import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDto(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        Set<Roles> roles
) {
    public UserDto(User user){
        this(user.getUsername(), user.getPassword(), user.getRoles());
    }
}
