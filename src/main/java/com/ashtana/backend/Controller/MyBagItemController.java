package com.ashtana.backend.Controller;


import com.ashtana.backend.DTO.RequestDTO.MyBagItemRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.MyBagItemResponseDTO;
import com.ashtana.backend.Service.MyBagItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bag_items")
@CrossOrigin(origins = "http://localhost:4200")
public class MyBagItemController {

    private final MyBagItemService myBagItemService;

    public MyBagItemController(MyBagItemService myBagItemService) {
        this.myBagItemService = myBagItemService;
    }

    @PostMapping
    public ResponseEntity<MyBagItemResponseDTO> createCartItem(@RequestBody MyBagItemRequestDTO dto) {
        return ResponseEntity.ok(myBagItemService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyBagItemResponseDTO> getCartItemById(@PathVariable Long id) {
        return ResponseEntity.ok(myBagItemService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MyBagItemResponseDTO>> getAllCartItems() {
        return ResponseEntity.ok(myBagItemService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long id) {
        return ResponseEntity.ok(myBagItemService.delete(id));
    }
}
