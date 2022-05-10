package com.exampleepaam.restaurant.model.dto;

import java.util.Objects;

/**
 * Response DTO class for OrderedItem
 */
public class OrderedItemResponseDto {

    String dishName;
    Integer dishesOrdered;


    public OrderedItemResponseDto() {
    }

    public OrderedItemResponseDto(String dishName, Integer dishesOrdered) {
        this.dishName = dishName;
        this.dishesOrdered = dishesOrdered;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getDishesOrdered() {
        return dishesOrdered;
    }

    public void setDishesOrdered(Integer dishesOrdered) {
        this.dishesOrdered = dishesOrdered;
    }

    @Override
    public String toString() {
        return "OrderedItemResponseDto{" +
                "dishName='" + dishName + '\'' +
                ", dishesOrdered=" + dishesOrdered +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedItemResponseDto that = (OrderedItemResponseDto) o;
        return Objects.equals(dishName, that.dishName) &&
                Objects.equals(dishesOrdered, that.dishesOrdered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishName, dishesOrdered);
    }
}

