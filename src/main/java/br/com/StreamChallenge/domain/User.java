package br.com.StreamChallenge.domain;

import br.com.StreamChallenge.dto.User.UserDto;
import br.com.StreamChallenge.dto.User.UserUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_entity")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
    @Getter
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Roles> roles = new HashSet<>();

    public User(UserDto userDto){
        this.login=userDto.login();
        this.password=userDto.password();
        this.roles =userDto.roles();
    }
    public User(UserUpdateDto userDto){
        this.password=userDto.password();
        this.roles =userDto.roles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public void passwordEncoder(){
        this.password= BCrypt.hashpw( this.password,BCrypt.gensalt());
    }
    public void addRoles(Roles roles){
        this.getRoles().add(roles);
    }

    public void merge(User user) {
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }
}
