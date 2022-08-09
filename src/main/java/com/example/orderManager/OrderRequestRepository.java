package com.example.orderManager;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRequestRepository extends CrudRepository<OrderRequest, Long> {
    List<OrderRequest> findByComplete(Boolean complete);
    List<OrderRequest> findByStockMovement(StockMovement stockMovement);
}
