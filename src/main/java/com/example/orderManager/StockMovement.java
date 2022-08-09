package com.example.orderManager;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;


@Entity
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    private Instant creationDate;
    @OneToOne
    private Item item;
    private Integer quantity;

    private Boolean visible;

    protected StockMovement() {};

    public StockMovement(Item item, Integer quantity, Boolean visible) {
        this.item = item;
        this.quantity = quantity;
        this.visible = visible;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
