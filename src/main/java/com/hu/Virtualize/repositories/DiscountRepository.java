package com.hu.Virtualize.repositories;

import com.hu.Virtualize.entities.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
}
