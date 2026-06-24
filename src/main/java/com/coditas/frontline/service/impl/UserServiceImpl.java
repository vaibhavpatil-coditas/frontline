package com.coditas.frontline.service.impl;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.request.UserRequest;
import com.coditas.frontline.dto.response.UserResponse;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.enums.Role;
import com.coditas.frontline.exception.ResourceAlreadyExistsException;
import com.coditas.frontline.mapper.UserMapper;
import com.coditas.frontline.repository.UserRepository;
import com.coditas.frontline.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerCustomer(UserRequest request) {
        return saveUser(request, Role.CUSTOMER);
    }

    @Override
    public UserResponse registerManager(UserRequest request) {
        return saveUser(request, Role.MANAGER);
    }

    @Override
    public UserResponse registerAgent(UserRequest request) {
        return saveUser(request, Role.AGENT);
    }

    private UserResponse saveUser(UserRequest request, Role role) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(request);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRegisteredOn(Instant.now());
        User savedCustomer = userRepository.save(user);
        return userMapper.toUserResponse(savedCustomer);
    }
}
