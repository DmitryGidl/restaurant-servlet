package com.exampleepaam.restaurant.dao.impl;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.dao.DishDao;
import com.exampleepaam.restaurant.dao.OrderDao;
import com.exampleepaam.restaurant.dao.UserDao;
import com.exampleepaam.restaurant.dao.mapper.DishDaoMapper;
import com.exampleepaam.restaurant.dao.mapper.OrderDaoMapper;
import com.exampleepaam.restaurant.dao.mapper.OrderItemDaoMapper;
import com.exampleepaam.restaurant.dao.mapper.UserDaoMapper;
import com.exampleepaam.restaurant.model.connectionpool.DBManager;

/**
 * Dao factory
 */
public class JDBCDaoFactory extends DaoFactory {
    DBManager dbManager = DBManager.getInstance();
    UserDaoMapper userDaoMapper = new UserDaoMapper();
    DishDaoMapper dishDaoMapper = new DishDaoMapper();
    OrderDaoMapper orderDaoMapper = new OrderDaoMapper();
    OrderItemDaoMapper orderItemDaoMapper = new OrderItemDaoMapper();

    /**
     * Returns a UserDao with the connection opened
     *
     * @return UserDao
     */
    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(dbManager.getConnection(),
                userDaoMapper);
    }

    /**
     * Returns a DishDao with the connection opened
     *
     * @return DishDao
     */
    @Override
    public DishDao createDishDao() {
        return new JDBCDishDao(dbManager.getConnection(),
                dishDaoMapper);
    }

    /**
     * Returns an OrderDao with the connection opened
     *
     * @return OrderDao
     */
    @Override
    public OrderDao createOrderDao() {
        return new JDBCOrderDao(dbManager.getConnection(), orderDaoMapper,
                userDaoMapper, orderItemDaoMapper);
    }
}
