package com.example.ztpai.service;

import com.example.ztpai.model.User;
import com.example.ztpai.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isPresent()){
            return new org.springframework.security.core.userdetails.User(
                    user.get().getEmail(), user.get().getPassword(),user.get().getEnabled(),
                    true, true, true,
                    getAuthorities(user.get().getRole()));
        }
        throw new UsernameNotFoundException("not found: " + email);
    }
    private Collection<? extends GrantedAuthority> getAuthorities(String authority){
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

}
