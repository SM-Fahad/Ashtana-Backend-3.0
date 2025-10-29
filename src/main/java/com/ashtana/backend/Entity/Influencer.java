package com.ashtana.backend.Entity;

import jakarta.persistence.*;

@Entity
public class Influencer {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String influencerCode; // coupon code
    private Double commissionRate;

    @OneToOne
    private User user;
}
