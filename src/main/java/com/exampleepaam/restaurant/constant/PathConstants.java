package com.exampleepaam.restaurant.constant;

/**
 * Global path constants
 */
public final class PathConstants {
    private PathConstants() {
    }

    // Auth
    public static final String SIGNUP_PATH = "/signup";
    public static final String LOGIN_PATH = "/login";
    public static final String USER_LOGOUT_PATH = "/logout";

    // User
    public static final String USER_MENU_PATH = "/menu";
    public static final String USER_ORDER_POST_PATH = "/orders";
    public static final String USER_ORDER_HISTORY_PATH = "/orders/my-history";
    public static final String USER_TOP_UP_PATH = "/top-up";


    // Admin
    public static final String ADMIN_NEW_DISH_PATH = "/admin/dishes/new";
    public static final String ADMIN_DELETE_DISH_PATH = "/admin/dishes/delete";
    public static final String ADMIN_UPDATE_DISH_PATH = "/admin/dishes/update";
    public static final String ADMIN_ORDER_PATH = "/admin/orders";
    public static final String ADMIN_DISH_PATH = "/admin/dishes";

    // Index
    public static final String INDEX_PAGE_PATH = "/";
    // Error
    public static final String ERROR_HANDLER_PATH = "/error";
}
