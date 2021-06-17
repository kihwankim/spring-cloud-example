package com.example.orderservice.domain.vo;

import lombok.Data;

@Data
public class RequsetOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
