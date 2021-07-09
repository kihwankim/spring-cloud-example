package com.example.userservice.controller;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.requset.UserRequset;
import com.example.userservice.vo.response.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;

    @GetMapping("/health-check")
    @Timed(value = "users.status", longTask = true)
    public String status(HttpServletRequest req) {
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        return "It's Working in User Service"
                + ", Port(local.server.port)=" + env.getProperty("local.server.port")
                + ", Port(server.port)=" + env.getProperty("server.port")
                + ", Port(token.expiration-time)=" + env.getProperty("token.expiration-time")
                + ", Requset uri = " + url;
    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequset userRequset) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userRequset, UserDto.class);
        UserDto savedResult = userService.createUser(userDto);
        ResponseUser responseUser = mapper.map(savedResult, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUserByAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> createUser(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserByUserid(userId));
    }
}
