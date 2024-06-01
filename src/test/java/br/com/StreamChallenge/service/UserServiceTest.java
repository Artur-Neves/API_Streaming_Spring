package br.com.StreamChallenge.service;

import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.repository.UserRepository;
import br.com.StreamChallenge.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.mbeans.UserMBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class UserServiceTest extends BaseServiceTest<VideoService> {
    @InjectMocks
    private UserService service;
    @Mock
    private Pageable pageable;
    @Mock
    private UserRepository repository;
    @Mock
    private User userMock;
    @Mock
    private AdminService adminService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Testando o login do usuario ")
    void test1() {
        User user = randomUser();
        BDDMockito.given(repository.findByLogin(user.getUsername())).willReturn(Optional.of(userMock));
        assertEquals(userMock, service.loadUserByUsername(user.getUsername()));
        then(repository).should().findByLogin(user.getUsername());
    }


    @Test
    @DisplayName("Testando se o usuario foi salvo e se ele chamou o adminservice")
    void test2() {
        User user = randomAdmin();
        BDDMockito.given(repository.save(user)).willReturn(user);
        assertEquals(user, service.save(user));
        then(adminService).should().adminAuthorization();
                    }

    @Test
    @DisplayName("Testando se o usuário não chama o adminService quando o usuario não possui a função de admin")
    void test3() {
        User user = randomUser();
        BDDMockito.given(repository.save(user)).willReturn(user);
        assertEquals(user, service.save(user));
        then(adminService).shouldHaveNoMoreInteractions();
    }
    @Test
    @DisplayName("Testando se esta chamando a classe que codifica a senha para o cadastro")
    void test4() {
        BDDMockito.given(repository.save(userMock)).willReturn(userMock);
        assertEquals(userMock, service.save(userMock));
        then(userMock).should().passwordEncoder();
    }
    @Test
    @DisplayName("Testando se esta chamando o método que atualiza a entidade")
    void test5() {
        User user= randomUser();
        BDDMockito.given(repository.findById(user.getId())).willReturn(Optional.of(userMock));
        assertEquals(userMock, service.update(user.getId(), user));
        then(userMock).should().merge(user);
    }
    @Test
    @DisplayName("Testando quando nem a entidade recebida nem a devolvida possuem o perfil de ADMIN")
    void test6() {
        User user =randomUser();
        BDDMockito.given(repository.findById(user.getId())).willReturn(Optional.of(userMock));
        assertEquals(userMock, service.update(user.getId(), user));
        then(adminService).shouldHaveNoMoreInteractions();
    }
    @Test
    @DisplayName("Testando se a entidade recebida tem o perfil de ADMIN")
    void test7() {
        User userUser =randomUser();
        User userAdmin = randomAdmin();
        BDDMockito.given(repository.findById(userAdmin.getId())).willReturn(Optional.of(userUser));
        assertEquals(userUser, service.update(userAdmin.getId(), userAdmin));
        then(adminService).should().adminAuthorization();
    }
    @Test
    @DisplayName("Testando se a entidade a ser atualizada tem o perfil de ADMIN")
    void test8() {
        User userUser =randomUser();
        User userAdmin = randomAdmin();
        BDDMockito.given(repository.findById(userUser.getId())).willReturn(Optional.of(userAdmin));
        assertEquals(userAdmin, service.update(userUser.getId(), userUser));
        then(adminService).should().adminAuthorization();
    }

    @Test
    @DisplayName("Testando o lançamento de uma exceção ao não achar um usuario com este login")
    void test9(){
        User user = randomUser();
        BDDMockito.given(repository.findByLogin(user.getUsername())).willReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,()-> service.loadUserByUsername(user.getUsername()));
        then(repository).should().findByLogin(user.getUsername());
    }
    @Test
    @DisplayName("Testando o lançamento de uma exceção ao não achar um usuario com este id")
    void test10(){
        User user = randomUser();
        BDDMockito.given(repository.findById(user.getId())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> service.findById(user.getId()));
        then(repository).should().findById(user.getId());
    }
    @Test
    @DisplayName("Testando a exclusão bem sucedida de um usuario")
    void test11(){
        User user = randomUser();
        given(repository.findById(anyLong())).willReturn(Optional.of(user));
        service.remove(anyLong());
        then(repository).should().delete(user);
        then(adminService).shouldHaveNoInteractions();
    }
    @Test
    @DisplayName("Testando a exclusão de um usuario inexistente de um usuario")
    void test12(){
        User user = randomUser();
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->service.remove(anyLong()));
    }
    @Test
    @DisplayName("Testando a exclusão de um Admin inexistente de um usuario")
    void test13(){
        User user = randomAdmin();
        given(repository.findById(anyLong())).willReturn(Optional.of(user));
        service.remove(anyLong());
        then(repository).should().delete(user);
        then(adminService).should().adminAuthorization();
    }

    private User randomUser(){
        return new User(1L, "Artur@Gmail.com", "123", Set.of(Roles.USER));
    }
    private User randomAdmin(){
        return new User(2L, "Artur@Gmail.com", "123", Set.of(Roles.ADMIN));
    }
}