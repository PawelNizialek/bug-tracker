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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        if(user.getEnabled())
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(),user.getEnabled(),
                    true, true, true,
                    authorities);
        else throw new UsernameNotFoundException("Account is blocked");
    }
    private Collection<? extends GrantedAuthority> getAuthorities(String authority){
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

}
