package com.example.orderservice.controller;

import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.vo.RequsetOrder;
import com.example.orderservice.domain.vo.ResponseOrder;
import com.example.orderservice.messagequeue.consumer.KafkaProducer;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {
    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Order Service on Port %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> saveOrder(@PathVariable("userId") String userId,
                                       @RequestBody RequsetOrder requsetOrder) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requsetOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto savedOrder = orderService.createOrder(orderDto);

        /**
         * send this order to the kafka
         */
        kafkaProducer.send("example-catalog-topic", orderDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.map(savedOrder, ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getAllOrdersByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(orderService.getAllOrdersByUserId(userId));
    }
}