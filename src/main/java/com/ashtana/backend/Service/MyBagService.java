package com.ashtana.backend.Service;

import com.ashtana.backend.DTO.RequestDTO.MyBagRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.MyBagResponseDTO;
import com.ashtana.backend.Entity.MyBag;
import com.ashtana.backend.Entity.MyBagItems;
import com.ashtana.backend.Entity.User;
import com.ashtana.backend.Repository.MyBagRepo;
import com.ashtana.backend.Repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MyBagService {

    private final MyBagRepo myBagRepo;
    private final UserRepo userRepo;

    public MyBagService(MyBagRepo myBagRepo, UserRepo userRepo) {
        this.myBagRepo = myBagRepo;
        this.userRepo = userRepo;
    }

    // ➤ Convert DTO → Entity
    public MyBag toEntity(MyBagRequestDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));
        MyBag myBag = new MyBag();
        myBag.setUser(user);
        return myBag;
    }

    // ➤ Convert Entity → DTO
    public MyBagResponseDTO toDto(MyBag myBag) {
        MyBagResponseDTO dto = new MyBagResponseDTO();
        dto.setId(myBag.getId());
        dto.setUserName(myBag.getUser().getUserName());
        dto.setTotalItems(myBag.getItems().stream().mapToInt(MyBagItems::getQuantity).sum());
        dto.setTotalPrice(myBag.getItems().stream().mapToDouble(MyBagItems::getTotalPrice).sum());
        return dto;
    }

    // ➤ Create Cart
    public MyBagResponseDTO createBag(MyBagRequestDTO dto) {
        MyBag saved = myBagRepo.save(toEntity(dto));
        return toDto(saved);
    }

    // ➤ Get Cart by ID
    public MyBagResponseDTO getMyBagById(Long id) {
        MyBag myBag = myBagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + id));
        return toDto(myBag);
    }

    // ➤ Get all Carts
    public List<MyBagResponseDTO> getAllMyBag() {
        return myBagRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // ➤ Delete Cart
    public String deleteFromBag(Long id) {
        MyBag myBag = myBagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("My Bag not found with ID: " + id));
        myBagRepo.delete(myBag);
        return "My Bag deleted successfully.";
    }

    // ➤ Recalculate total price (optional helper for CartItems)
    public void recalculateTotal(MyBag myBag) {
        double total = myBag.getItems().stream().mapToDouble(MyBagItems::getTotalPrice).sum();
        myBag.setTotalPrice(total);
        myBagRepo.save(myBag);
    }

}
