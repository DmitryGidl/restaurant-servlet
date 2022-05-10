package com.exampleepaam.restaurant.dao.mapper;

import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet to Order mapper
 */
public class OrderDaoMapper implements ObjectDaoMapper<Order> {
    @Override
    public Order extractFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setAddress(rs.getString("address"));
        order.setStatus(Status.valueOf(rs.getString("status")));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setCreationDateTime(rs.getTimestamp("creation_date_time").toLocalDateTime());
        order.setUpdateDateTime(rs.getTimestamp("update_date_time").toLocalDateTime());
        order.setUpdateDateTime(rs.getTimestamp("update_date_time").toLocalDateTime());
        return order;
    }
}
