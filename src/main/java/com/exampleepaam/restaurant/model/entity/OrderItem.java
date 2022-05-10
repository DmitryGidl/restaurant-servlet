package com.exampleepaam.restaurant.model.entity;

/**
 * Describes OrderItem entity
 */
public class OrderItem extends AbstractBaseEntity {

    private String dishName;
    private Integer quantity;
    private Order order;

    public OrderItem(long id, String dishName, Integer quantity, Order order) {
        super(id);
        this.dishName =dishName;
        this.quantity = quantity;
        this.order = order;
    }

    public OrderItem(String dishName, Integer quantity, Dish dish, Order order) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.order = order;
    }

    public OrderItem() {
    }

    public OrderItem(String dishName, int quantity) {
        this.dishName = dishName;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem(String dishName, Integer quantity) {
        this.dishName = dishName;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
