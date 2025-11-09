package org.jwt.jwtexample.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("aks".equals(username)) {
            return new User("aks", "{noop}SillyPassword", List.of(()->"ROLE_ADMIN"));
        } else if ("someone".equals(username)) {
            return new User("someone", "{noop}SillyPassword", List.of(()->"ROLE_USER"));
        }
        throw new UsernameNotFoundException("User not found" + username);
    }
}
