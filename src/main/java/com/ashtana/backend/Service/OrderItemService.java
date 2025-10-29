package com.ashtana.backend.Service;


import com.ashtana.backend.DTO.RequestDTO.OrderItemRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.OrderItemResponseDTO;
import com.ashtana.backend.Entity.Order;
import com.ashtana.backend.Entity.OrderItem;
import com.ashtana.backend.Entity.Product;
import com.ashtana.backend.Repository.OrderItemRepo;
import com.ashtana.backend.Repository.OrderRepo;
import com.ashtana.backend.Repository.ProductRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    private final OrderItemRepo orderItemRepo;

    private final ProductRepo productRepo;

    private final OrderRepo orderRepo;

    public OrderItemService(OrderItemRepo orderItemRepo, ProductRepo productRepo, OrderRepo orderRepo) {
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    // ✅ Entity → DTO convert method
    public OrderItemResponseDTO toDto(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPricePerItem(item.getProduct().getPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    // ✅ DTO → Entity convert method
    public OrderItem toEntity(OrderItemRequestDTO dto, Order order) {
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setTotalPrice(product.getPrice() * dto.getQuantity());
        return orderItem;
    }

    // ✅ Save item
    public OrderItemResponseDTO saveOrderItem(OrderItemRequestDTO dto) {

        Order order = orderRepo.findById(dto.getOrder_id())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + dto.getOrder_id()));

        OrderItem orderItem = toEntity(dto, order);
        orderItemRepo.save(orderItem);

        return toDto(orderItem);
    }

}
