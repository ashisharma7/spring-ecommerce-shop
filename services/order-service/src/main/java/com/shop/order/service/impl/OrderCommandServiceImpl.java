package com.shop.order.service.impl;

import com.shop.order.service.OrderCommandService;
import com.shop.order.web.dto.CreateOrderRequest;
import com.shop.order.web.dto.CreateOrderResponse;
import jakarta.el.MethodNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest){
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
