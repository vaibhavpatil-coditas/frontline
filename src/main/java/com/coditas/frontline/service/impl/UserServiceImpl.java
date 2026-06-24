package com.coditas.frontline.service.impl;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.request.CustomerRequest;
import com.coditas.frontline.dto.request.UserRequest;
import com.coditas.frontline.dto.response.CustomerResponse;
import com.coditas.frontline.dto.response.UserResponse;
import com.coditas.frontline.entity.Customer;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.enums.Role;
import com.coditas.frontline.exception.ResourceAlreadyExistsException;
import com.coditas.frontline.mapper.CustomerMapper;
import com.coditas.frontline.mapper.UserMapper;
import com.coditas.frontline.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse registerCustomer(CustomerRequest request) {
        User user = saveUser(request.getUser(), Role.CUSTOMER);
        Customer customer = customerMapper.toCustomer(request);
        customer.setRegisteredOn(Instant.now());
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(savedCustomer);
    }

    @Override
    public UserResponse registerManager(UserRequest request) {
        User user = saveUser(request, Role.MANAGER);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse registerAgent(UserRequest request) {
        User user = saveUser(request, Role.AGENT);
        return userMapper.toUserResponse(user);
    }

    private User saveUser(UserRequest request, Role role) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(request);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
}
