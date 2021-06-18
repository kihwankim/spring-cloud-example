package com.example.userservice.service;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.vo.response.ResponseUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserid(String userId);

    List<ResponseUser> getUserByAll();
}
