package com.coditas.frontline.security.users;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException(ExceptionMessage.EMAIL_NOT_FOUND));
    }
}
