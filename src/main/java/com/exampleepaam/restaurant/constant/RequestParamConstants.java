package com.exampleepaam.restaurant.constant;

/**
 * Request parameters constants
 */
public final class RequestParamConstants {
    private RequestParamConstants() {
    }

    // Generic
    public static final String ID_PARAM = "id";

    // User
    public static final String USER_PARAM = "user";
    public static final String USER_PASSWORD_PARAM = "password";
    public static final String USER_EMAIL_PARAM = "email";
    public static final String USER_NAME_PARAM = "name";
    public static final String AUTH_REMEMBER_PARAM = "remember";
    public static final String USER_BALANCE_PARAM = "balance";
    public static final String AUTH_MATCHING_PASSWORD_PARAM = "matchingPassword";

    // Table
    public static final String SORT_FIELD_PARAM = "sortField";
    public static final String SORT_DIR_PARAM = "sortDir";
    public static final String FILTER_CATEGORY_PARAM = "filterCategory";
    public static final String FILTER_STATUS_PARAM = "filterStatus";
    public static final String REVERSE_SORT_DIR_ATTRIBUTE = "reverseSortDir";
    public static final String PAGE_SIZE_PARAM = "pageSize";
    public static final String CURRENT_PAGE_PARAM = "currentPage";

    // Dish
    public static final String DISHES_ATTRIBUTE = "dishes";
    public static final String DISH_ATTRIBUTE = "dish";
    public static final String DISH_PAGED_ATTRIBUTE = "dishPaged";
    public static final String DISH_NAME_PARAM = "name";
    public static final String DISH_DESCRIPTION_PARAM = "description";
    public static final String DISH_CATEGORY_PARAM = "category";
    public static final String DISH_PRICE_PARAM = "price";
    public static final String DISH_IMAGE_PARAM = "image";

    // Order
    public static final String ORDER_ADDRESS_PARAM = "address";
    public static final String ORDER_MAP_PARAM = "dishIdQuantityMap";
    public static final String ORDER_PAGED_ATTRIBUTE = "orderPaged";


}


