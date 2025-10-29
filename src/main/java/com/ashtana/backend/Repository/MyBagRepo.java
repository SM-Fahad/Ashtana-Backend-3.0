package com.ashtana.backend.Repository;


import com.ashtana.backend.Entity.MyBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyBagRepo extends JpaRepository<MyBag, Long> {
}
