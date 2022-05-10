package com.exampleepaam.restaurant.service;

public class ServiceManager {
    private static ServiceManager instance;
    private DishService dishService;
    private OrderService orderService;
    private  UserService userService;

    private ServiceManager() {
        init();
    }

    public static synchronized ServiceManager getInstance() {
        if (instance == null) instance = new ServiceManager();
        return instance;
    }

    public void init() {
        dishService = new DishService();
        userService = new UserService();
        orderService = new OrderService();

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
