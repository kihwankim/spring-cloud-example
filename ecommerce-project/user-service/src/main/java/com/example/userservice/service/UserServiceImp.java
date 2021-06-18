package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.response.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = mapper.map(userDto, User.class);
        user.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepository.save(user);

        return new ModelMapper().map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByUserid(String userId) {
        Optional<User> findUser = userRepository.findByUserId(userId);
        User user = findUser
                .orElseThrow(() -> new UsernameNotFoundException("User not found Exception"));

        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public List<ResponseUser> getUserByAll() {
        List<ResponseUser> responseUsers = new ArrayList<>();
        userRepository.findAll().forEach(v ->
                responseUsers.add(new ModelMapper().map(v, ResponseUser.class)));
        return responseUsers;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Not found User email : %s", email)));
        return null;
    }
}
