package br.com.StreamChallenge.dto.User;

import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserDtoComplete(
        Long id,
        String login,
        Set<Roles> roles
) {
    public UserDtoComplete(User user){
        this(user.getId(), user.getUsername(), user.getRoles());
    }
}
