package br.com.StreamChallenge.dto.User;

import br.com.StreamChallenge.domain.User;

public record UserLoginDto (
        String login,
        String password
){
    public UserLoginDto(User user){
    this(user.getUsername(), user.getPassword());
    }
}
