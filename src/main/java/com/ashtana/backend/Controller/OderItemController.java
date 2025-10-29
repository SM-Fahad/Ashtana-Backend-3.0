package com.ashtana.backend.Controller;


import com.ashtana.backend.Service.OrderItemService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order_items")
@CrossOrigin(origins = "http://localhost:4200")
public class OderItemController {

    private final OrderItemService orderItemService;

    public OderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }
}
