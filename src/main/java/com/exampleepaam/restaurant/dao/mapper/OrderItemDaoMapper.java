package com.exampleepaam.restaurant.dao.mapper;

import com.exampleepaam.restaurant.model.entity.OrderItem;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet to OrderItem mapper
 */
public class OrderItemDaoMapper implements ObjectDaoMapper<OrderItem> {

    @Override
    public OrderItem extractFromResultSet(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong("id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setDishName(rs.getString("dish_name"));
        return orderItem;
    }
}
