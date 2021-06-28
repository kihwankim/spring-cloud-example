package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.domain.User;
import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.response.ResponseOrder;
import com.example.userservice.vo.response.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    private final Environment env;
    private final RestTemplate restTemplate;

    // FeignClient 로 통신
    private final OrderServiceClient orderServiceClient;

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
        UserDto userDto = new ModelMapper().map(user, UserDto.class);

//        /* Using RestTemplate 사용 */
//        String orderUrl = String.format(Objects.requireNonNull(env.getProperty("order-service.url")), userId);
//        ResponseEntity<List<ResponseOrder>> ordersResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });
//
//        List<ResponseOrder> orders = ordersResponse.getBody();

        /* Feign client */
        List<ResponseOrder> orders = orderServiceClient.getOrdersByUserId(userId);

        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public List<ResponseUser> getUserByAll() {
        List<ResponseUser> responseUsers = new ArrayList<>();
        userRepository.findAll().forEach(v ->
                responseUsers.add(new ModelMapper().map(v, ResponseUser.class)));
        return responseUsers;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found Exception email -> %s", email)));

        return new ModelMapper().map(findUser, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Not found User email : %s", email)));

        return new org.springframework.security.core.userdetails.User(findUser.getEmail(), findUser.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
        // 마지막에 권한
    }
}
