package com.exampleepaam.restaurant.model.dto;

import java.util.Map;
import java.util.Objects;

/**
 * Creation DTO class for Order
 */
public class OrderCreationDto {
    private String address;
    private Map<Long, Integer> dishIdQuantityMap;

    public OrderCreationDto(String address, Map<Long, Integer> dishIdQuantityMap) {
        this.address = address;
        this.dishIdQuantityMap = dishIdQuantityMap;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<Long, Integer> getDishIdQuantityMap() {
        return dishIdQuantityMap;
    }

    public void setDishIdQuantityMap(Map<Long, Integer> dishIdQuantityMap) {
        this.dishIdQuantityMap = dishIdQuantityMap;
    }

    @Override
    public String toString() {
        return "OrderCreationDto{" +
                "address='" + address + '\'' +
                ", dishIdQuantityMap=" + dishIdQuantityMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCreationDto that = (OrderCreationDto) o;
        return Objects.equals(address, that.address) &&
                Objects.equals(dishIdQuantityMap, that.dishIdQuantityMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, dishIdQuantityMap);
    }
}

