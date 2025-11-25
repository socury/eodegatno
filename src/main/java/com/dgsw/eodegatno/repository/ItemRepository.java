package com.dgsw.eodegatno.repository;

import com.dgsw.eodegatno.domain.ItemEntity;
import com.dgsw.eodegatno.domain.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    /** JPQL */
    @Query("SELECT i FROM ItemEntity i ORDER BY i.createdAt DESC ")
    List<ItemEntity> findAllByOrderByCreatedAtDesc();

    /** QueryDSL */
    List<ItemEntity> findByStatus(ItemStatus status);
}
