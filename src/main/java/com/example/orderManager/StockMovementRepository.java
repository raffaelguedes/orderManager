package com.example.orderManager;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockMovementRepository extends CrudRepository<StockMovement, Long> {
    List<StockMovement> findByVisible(Boolean visible);
}
