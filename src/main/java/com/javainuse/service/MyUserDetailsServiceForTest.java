package com.javainuse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("test")
public class MyUserDetailsServiceForTest implements UserDetailsService {
    List<User> users = new ArrayList<>();

    @Autowired
    private void init(PasswordEncoder passwordEncoder) {
        List<GrantedAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User("user", passwordEncoder.encode("pass1"), userAuthorities);
        List<GrantedAuthority> adminAuthorities = new ArrayList<>();
        adminAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        adminAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        User admin = new User("admin", passwordEncoder.encode("pass2"), adminAuthorities);
        users.add(admin);
        users.add(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (User user : users)
            if (username.equals(user.getUsername()))
                return user;
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}