package com.example.orderservice.controller;

import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.vo.RequsetOrder;
import com.example.orderservice.domain.vo.ResponseOrder;
import com.example.orderservice.messagequeue.consumer.KafkaProducer;
import com.example.orderservice.messagequeue.producer.OrderProducer;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {
    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Order Service on Port %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> saveOrder(@PathVariable("userId") String userId,
                                       @RequestBody RequsetOrder requsetOrder) {
        log.info("Before add orders data");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requsetOrder, OrderDto.class);
        orderDto.setUserId(userId);
        /** jpa
         */
        //        OrderDto savedOrder = orderService.createOrder(orderDto);

        /**
         * kafka
         */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(requsetOrder.getQty() * requsetOrder.getUnitPrice());

        /**
         * send this order to the kafka
         */
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        log.info("After add orders data");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.map(orderDto, ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getAllOrdersByUserId(@PathVariable("userId") String userId) {
        log.info("Before retrieve order data");
        List<ResponseOrder> allOrdersByUserId = orderService.getAllOrdersByUserId(userId);
        log.info("After retrieve order data");

        return ResponseEntity.ok(allOrdersByUserId);
    }
}