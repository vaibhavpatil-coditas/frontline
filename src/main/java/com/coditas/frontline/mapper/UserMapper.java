package com.coditas.frontline.mapper;

import com.coditas.frontline.dto.request.UserRequest;
import com.coditas.frontline.dto.response.UserResponse;
import com.coditas.frontline.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);
    UserResponse toUserResponse(User savedUser);
}
