package com.example.coderlab.security;

import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<UserEntity> user= userRepository.findByEmail(email);
        if (user.isPresent()){
            return new CustomUserDetails(user.get());
        }
      else {
            throw new UsernameNotFoundException("Could not find user");
        }
    }

}