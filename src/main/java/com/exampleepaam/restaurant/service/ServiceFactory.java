package com.exampleepaam.restaurant.service;

/**
 * Service Factory
 */
public class ServiceFactory {
    private static ServiceFactory instance;
    private final DishService dishService = new DishService();
    private final OrderService orderService = new OrderService();
    private  final UserService userService = new UserService();

    private ServiceFactory() {
    }

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) instance = new ServiceFactory();
        return instance;
    }

    public DishService getDishService() {
        return dishService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public UserService getUserService() {
        return userService;
    }
}
