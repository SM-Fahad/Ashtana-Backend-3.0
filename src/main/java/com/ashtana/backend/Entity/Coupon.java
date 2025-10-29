package com.ashtana.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Double discountPercentage;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    @ManyToOne
    private Influencer influencer;
}
