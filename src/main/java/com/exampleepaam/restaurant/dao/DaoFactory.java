package com.exampleepaam.restaurant.dao;

import com.exampleepaam.restaurant.dao.impl.JDBCDaoFactory;

/**
 * Abstract Dao factory
 */
public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract UserDao createUserDao();

    public abstract DishDao createDishDao();

    public abstract OrderDao createOrderDao();

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                daoFactory = new JDBCDaoFactory();

            }
        }
        return daoFactory;
    }
}
