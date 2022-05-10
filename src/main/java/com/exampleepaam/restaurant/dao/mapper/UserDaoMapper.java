package com.exampleepaam.restaurant.dao.mapper;

import com.exampleepaam.restaurant.model.entity.Role;
import com.exampleepaam.restaurant.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet to User mapper
 */
public class UserDaoMapper implements ObjectDaoMapper<User> {
    @Override
    public User extractFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setBalanceUAH(rs.getBigDecimal("balanceuah"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setPassword(rs.getString("password"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
    }
}
