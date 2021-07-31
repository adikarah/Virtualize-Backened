package com.hu.Virtualize.repositories;

import com.hu.Virtualize.entities.UserInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterestEntity, Long> {
}
