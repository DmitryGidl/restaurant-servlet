package com.exampleepaam.restaurant.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Response DTO class for Order
 */
public class OrderResponseDto {
    private long id;
    private StatusDto status;
    private String address;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
    private BigDecimal totalPrice;
    private List<OrderedItemResponseDto> orderItems;

    public OrderResponseDto() {
    }

    public OrderResponseDto(long id, StatusDto status, String address,
                            LocalDateTime creationDateTime, LocalDateTime updateDateTime,
                            BigDecimal totalPrice, List<OrderedItemResponseDto> orderItems) {
        this.id = id;
        this.status = status;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.updateDateTime = updateDateTime;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
    }

    public enum StatusDto {
        PENDING,
        COOKING,
        DELIVERING,
        COMPLETED,
        DECLINED
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StatusDto getStatus() {
        return status;
    }

    public void setStatus(StatusDto status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderedItemResponseDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderedItemResponseDto> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponseDto that = (OrderResponseDto) o;
        return id == that.id && status == that.status && Objects.equals(address, that.address) &&
                Objects.equals(totalPrice, that.totalPrice) && Objects.equals(orderItems, that.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, address, totalPrice, orderItems);
    }
}
