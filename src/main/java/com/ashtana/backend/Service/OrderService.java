package com.ashtana.backend.Service;


import com.ashtana.backend.DTO.RequestDTO.OrderItemRequestDTO;
import com.ashtana.backend.DTO.RequestDTO.OrderRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.OrderItemResponseDTO;
import com.ashtana.backend.DTO.ResponseDTO.OrderResponseDTO;
import com.ashtana.backend.Entity.*;
import com.ashtana.backend.Enums.OrderStatus;
import com.ashtana.backend.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

    private final AddressRepo addressRepo;

    private final MyBagItemsRepo myBagItemsRepo;


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;


    public Order createOrder(OrderRequestDTO dto) {
        User user = userRepo.findById(dto.getUserName()).orElseThrow();
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(dto.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);

        Double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            Product product = productRepo.findById(itemDTO.getProductId()).orElseThrow();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setTotalPrice(product.getPrice() * itemDTO.getQuantity());

            totalAmount += orderItem.getTotalPrice();
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
//        order.setItems(orderItems);

        Order savedOrder = orderRepo.save(order);
        orderItemRepo.saveAll(orderItems);

        return savedOrder;
    }


    // ✅ Convert Entity → DTO
    public OrderResponseDTO toDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserName(order.getUser().getUserName());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());

        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
            OrderItemResponseDTO i = new OrderItemResponseDTO();
            i.setProductId(item.getProduct().getId());
            i.setProductName(item.getProduct().getName());
            i.setQuantity(item.getQuantity());
            i.setTotalPrice(item.getTotalPrice());
            return i;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    // ✅ Save Order + Return DTO
    public OrderResponseDTO save(OrderRequestDTO dto) {
        return toDto(createOrder(dto));
    }

    // ✅ Find by ID
    public OrderResponseDTO findById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return toDto(order);
    }

    // ✅ Find all Orders
    public List<OrderResponseDTO> findAll() {
        return orderRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ✅ Update Status (optional)
    public OrderResponseDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        return toDto(orderRepo.save(order));
    }

    // ✅ Delete
    public void delete(Long id) {
        orderRepo.deleteById(id);
    }

    @Transactional
    public OrderResponseDTO checkout(String userName, Long addressId) {
        // 1️⃣ Fetch User
        User user = userRepo.findById(userName)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userName));

        // 2️⃣ Fetch Shipping Address
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Shipping address not found with id: " + addressId));

        // 3️⃣ Fetch Cart Items
        List<MyBagItems> myBagItemsItems = myBagItemsRepo.findByUser_UserName(userName);
        if (myBagItemsItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Add products before checkout.");
        }

        // 4️⃣ Create new Order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setStatus(OrderStatus.PENDING);

        Double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 5️⃣ Convert CartItems → OrderItems
        for (MyBagItems cartItem : myBagItemsItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setTotalPrice(cartItem.getProduct().getPrice()
                    *(Double.valueOf(cartItem.getQuantity())));

            totalAmount = totalAmount+(orderItem.getTotalPrice());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 6️⃣ Save Order (cascades OrderItems)
        Order savedOrder = orderRepo.save(order);

        // 7️⃣ Clear User's Cart
        myBagItemsRepo.deleteAll(myBagItemsItems);

        // 8️⃣ Return OrderResponseDTO
        return this.toDto(savedOrder);
    }
}
