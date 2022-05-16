package com.exampleepaam.restaurant.service;

/**
 * Shared service instances
 */
public class SharedServices {
    private static SharedServices instance;
    private final DishService dishService = new DishService();
    private final OrderService orderService = new OrderService();
    private  final UserService userService = new UserService();

    private SharedServices() {
    }

    public static synchronized SharedServices getInstance() {
        if (instance == null) instance = new SharedServices();
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
