package com.example.userservice.domain.dto;

import com.example.userservice.vo.response.ResponseOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private LocalDateTime createAt;
    private String encryptedPwd;

    private List<ResponseOrder> orders = new ArrayList<>();

    public void addOrders(ResponseOrder... orders) {
        this.orders.addAll(Arrays.asList(orders));
    }
}
