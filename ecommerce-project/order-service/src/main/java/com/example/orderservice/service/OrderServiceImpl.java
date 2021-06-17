package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.vo.ResponseOrder;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDetails) {
        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Order order = mapper.map(orderDetails, Order.class);
        Order saveOrder = orderRepository.save(order);

        return mapper.map(saveOrder, OrderDto.class);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Order findOrder = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException(String.format("order not found exception id : %s", orderId)));

        return new ModelMapper().map(findOrder, OrderDto.class);
    }

    @Override
    public List<ResponseOrder> getAllOrdersByUserId(String userId) {
        List<Order> ordersByUser = orderRepository.findByUserId(userId);
        List<ResponseOrder> responseOrders = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ordersByUser.forEach(v -> responseOrders.add(mapper.map(v, ResponseOrder.class)));

        return responseOrders;
    }
}
