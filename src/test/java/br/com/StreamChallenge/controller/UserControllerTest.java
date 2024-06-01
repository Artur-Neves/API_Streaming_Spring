package br.com.StreamChallenge.controller;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import br.com.StreamChallenge.dto.User.UserDto;
import br.com.StreamChallenge.dto.User.UserDtoComplete;
import br.com.StreamChallenge.dto.User.UserLoginDto;
import br.com.StreamChallenge.dto.User.UserUpdateDto;
import br.com.StreamChallenge.dto.category.CategoryCompleteDto;
import br.com.StreamChallenge.dto.category.CategoryDto;
import br.com.StreamChallenge.infra.token.TokenDto;
import br.com.StreamChallenge.infra.token.TokenService;
import br.com.StreamChallenge.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest  extends  BaseControllerTest<UserService>{
    @Autowired
    JacksonTester<UserDto> userDtoJacksonTester;
    @Autowired
    JacksonTester<UserUpdateDto> userUpdateDtoJacksonTester;
    @Autowired
    JacksonTester<UserLoginDto> userLoginDtoJacksonTester;
    @Autowired
    JacksonTester<TokenDto> tokenServiceJacksonTester;
    @Autowired
    JacksonTester<UserDtoComplete> userDtoCompleteJacksonTester;
    @MockBean
    private AuthenticationManager manager;
    @Mock
    private Authentication authentication;
    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("Testando o login do usuário com as credenciais corretas")
    void loginCorrect() throws Exception {
        User user = randomUser();
        BDDMockito.given(manager.authenticate(any())).willReturn(authentication);
        BDDMockito.given(tokenService.createdToken(any())).willReturn("Token de acesso");
        mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJacksonTester.write(new UserDto(user)).getJson()))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(tokenServiceJacksonTester.write(new TokenDto("Token de acesso")).getJson()));
    }
    @Test
    void test2() throws Exception {
        UserDto userDto = new UserDto(randomUser());
        when(service.save(any())).thenReturn(randomUser());
        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJacksonTester.write(userDto).getJson()))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(
                                userDtoCompleteJacksonTester.write(new UserDtoComplete(randomUser())).getJson())
                        );

    }
    @Test
    @WithMockUser
    void test3() throws Exception {
        User user = randomUser();
        BDDMockito.given(service.update(eq(user.getId()), any(User.class))).willReturn(user);
        mvc.perform(put("/user/"+user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateDtoJacksonTester.write( new UserUpdateDto(user)).getJson()))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON));
    }
    @WithMockUser()
    @DisplayName("Ao realizar o delete, mas passando um tipo diferente do que o long " +
            "Deve-se retornar um badRequest")
    @Test
    void test4() throws Exception {
        String id ="Artur";
        mvc.perform(delete("/user/"+id))
                .andExpectAll(status().isBadRequest());

    }
    @WithMockUser()
    @DisplayName("Ao realizar o delete, mas passando um id que não existe no banco de dados " +
            "Deve-se retornar um error 404")
    @Test
    void test5() throws Exception {

        mvc.perform(delete("/user/"))
                .andExpectAll(status().isNotFound());
    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete " +
            "Deve-se retornar o codigo 204 e não deve retornar nada")
    @Test
    void test6() throws Exception {
        User user = randomUser();
        mvc.perform(delete("/user/"+user.getId()))
                .andExpectAll(status().isNoContent());
        then(service).should().remove(user.getId());
    }

    private User randomUser(){
        return new User(1L, "Artur@Gmail.com", "123", Set.of(Roles.USER));
    }
    private User randomAdmin(){
        return new User(1L, "Artur@Gmail.com", "123", Set.of(Roles.ADMIN));
    }
}