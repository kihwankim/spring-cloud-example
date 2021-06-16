package com.example.userservice.service;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.vo.response.ResponseUser;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserid(String userId);

    List<ResponseUser> getUserByAll();
}
