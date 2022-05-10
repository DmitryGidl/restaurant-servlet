package com.exampleepaam.restaurant.dao.mapper;

import com.exampleepaam.restaurant.model.entity.Category;
import com.exampleepaam.restaurant.model.entity.Dish;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSet to Dish mapper
 */
public class DishDaoMapper implements ObjectDaoMapper<Dish> {
    @Override
    public Dish extractFromResultSet(ResultSet rs) throws SQLException {
        Dish dish = new Dish();
        dish.setId(rs.getLong("id"));
        dish.setName(rs.getString("name"));
        dish.setDescription(rs.getString("description"));
        dish.setCategory(Category.valueOf(rs.getString("category")));
        dish.setPrice(rs.getBigDecimal("price"));
        dish.setImageFileName(rs.getString("image_file_name"));
        return dish;
    }


}
