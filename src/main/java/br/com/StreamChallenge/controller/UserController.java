package br.com.StreamChallenge.controller;

import br.com.StreamChallenge.domain.User;
import br.com.StreamChallenge.dto.User.UserDto;
import br.com.StreamChallenge.dto.User.UserDtoComplete;
import br.com.StreamChallenge.dto.User.UserLoginDto;
import br.com.StreamChallenge.dto.User.UserUpdateDto;
import br.com.StreamChallenge.infra.token.TokenDto;
import br.com.StreamChallenge.infra.token.TokenService;
import br.com.StreamChallenge.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @PostMapping()

     ResponseEntity<UserDtoComplete> saveUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder){
        User user  = service.save(new User(userDto));
        URI uri = uriComponentsBuilder.path("/{id}").buildAndExpand(user).toUri();
        return ResponseEntity.created(uri).body(new UserDtoComplete(user));
    }
    @PostMapping("/login")
    ResponseEntity<TokenDto> login (@Valid @RequestBody UserLoginDto dto){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        Authentication authentication = manager.authenticate(authenticationToken);
        return ResponseEntity.ok(new TokenDto(
                tokenService.createdToken((User) authentication.getPrincipal())));
    }
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<UserDto> update (@Valid @RequestBody UserUpdateDto userDto, @PathVariable long id){
       return  ResponseEntity.ok( new UserDto(service.update(id, new User(userDto))));
    }
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<?> deleteUser (@PathVariable Long id){
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
