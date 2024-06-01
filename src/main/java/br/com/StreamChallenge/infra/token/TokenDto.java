package br.com.StreamChallenge.infra.token;

public record TokenDto (
        String token
){
    public TokenDto(String token) {
        this.token=token;
    }
}
