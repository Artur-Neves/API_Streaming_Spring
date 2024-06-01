package br.com.StreamChallenge.service;

import br.com.StreamChallenge.domain.Roles;
import br.com.StreamChallenge.domain.User;
import br.com.StreamChallenge.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private AdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username)  {
        return repository.findByLogin(username).orElseThrow(()-> new UsernameNotFoundException("Credenciais inválidas"));
    }

    public User save(User user) {
        user.passwordEncoder();
        verifyAdminRole(user);
        return repository.save(user);
    }
    private void verifyAdminRole(User user){
        if(user.getRoles().contains(Roles.ADMIN)){
            adminService.adminAuthorization();
        }
    }
    @Transactional
    public User update(long id, User user) {
        User u = findById(id);
        verifyAdminRole(user);
        verifyAdminRole(u);
        u.merge(user);
        return u;
    }
    public User findById(Long id){
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));
    }

    public void remove(Long id) {
        User user = findById(id);
        verifyAdminRole(user);
        repository.delete(user);
    }
}
