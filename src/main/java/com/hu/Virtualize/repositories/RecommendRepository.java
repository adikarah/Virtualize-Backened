package com.hu.Virtualize.repositories;

import com.hu.Virtualize.entities.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendEntity,Long> {
}
