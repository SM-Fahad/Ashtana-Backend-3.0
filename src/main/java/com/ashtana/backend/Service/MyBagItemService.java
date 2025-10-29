package com.ashtana.backend.Service;


import com.ashtana.backend.DTO.RequestDTO.MyBagItemRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.MyBagItemResponseDTO;
import com.ashtana.backend.Entity.MyBag;
import com.ashtana.backend.Entity.MyBagItems;
import com.ashtana.backend.Entity.Product;
import com.ashtana.backend.Repository.MyBagItemsRepo;
import com.ashtana.backend.Repository.MyBagRepo;
import com.ashtana.backend.Repository.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyBagItemService {

    private final MyBagItemsRepo myBagItemRepo;
    private final MyBagRepo myBagRepo;
    private final ProductRepo productRepo;
    private final MyBagService myBagService; // To recalc total

    public MyBagItemService(MyBagItemsRepo myBagItemRepo, MyBagRepo myBagRepo, ProductRepo productRepo, MyBagService myBagService) {
        this.myBagItemRepo = myBagItemRepo;
        this.myBagRepo = myBagRepo;
        this.productRepo = productRepo;
        this.myBagService = myBagService;
    }

    // ➤ Convert DTO → Entity
    public MyBagItems toEntity(MyBagItemRequestDTO dto) {
        MyBag myBag = myBagRepo.findById(dto.getMyBagId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        MyBagItems item = new MyBagItems();
        item.setMyBag(myBag);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setTotalPrice(product.getPrice() * dto.getQuantity());

        return item;
    }

    // ➤ Convert Entity → DTO
    public MyBagItemResponseDTO toDto(MyBagItems item) {
        MyBagItemResponseDTO dto = new MyBagItemResponseDTO();
        dto.setId(item.getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    // ➤ Save CartItem
    public MyBagItemResponseDTO save(MyBagItemRequestDTO dto) {
        MyBagItems item = toEntity(dto);
        MyBagItems saved = myBagItemRepo.save(item);
        myBagService.recalculateTotal(item.getMyBag()); // recalc total
        return toDto(saved);
    }

    // ➤ Get CartItem by ID
    public MyBagItemResponseDTO getById(Long id) {
        MyBagItems item = myBagItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));
        return toDto(item);
    }

    // ➤ Get all CartItems
    public List<MyBagItemResponseDTO> getAll() {
        return myBagItemRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // ➤ Delete CartItem
    public String delete(Long id) {
        MyBagItems item = myBagItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("My Bag Items not found"));
        MyBag myBag = item.getMyBag();
        myBagItemRepo.delete(item);
        myBagService.recalculateTotal(myBag);
        return "Cart item deleted successfully!";
    }
}
