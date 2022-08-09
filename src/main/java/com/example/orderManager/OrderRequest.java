package com.example.orderManager;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class OrderRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    private Instant creationDate;

    @OneToOne
    private StockMovement stockMovement;

    private Integer quantity;

    @ManyToOne
    private Customer customer;

    private Boolean complete;

    protected OrderRequest(){}
    public OrderRequest(StockMovement stockMovement, Integer quantity, Customer customer, Boolean complete) {
        this.stockMovement = stockMovement;
        this.quantity = quantity;
        this.customer = customer;
        this.complete = complete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public StockMovement getStockMovement() {
        return stockMovement;
    }

    public void setStockMovement(StockMovement stockMovement) {
        this.stockMovement = stockMovement;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
