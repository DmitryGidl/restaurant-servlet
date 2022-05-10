package com.exampleepaam.restaurant.constant;

/**
 * Class that holds error constants
 * Values are used as keys for internalization
 */
public final class ErrorAttributeConstants {
    private ErrorAttributeConstants() {
    }

    // Attribute Keys
    public static final String ERROR_ATTRIBUTE_NAME = "errorName";
    public static final String ERROR_ATTRIBUTE_DESCRIPTION = "errorDescription";
    public static final String ERROR_ATTRIBUTE_PRICE = "errorPrice";
    public static final String ERROR_ATTRIBUTE_GLOBAL = "error";
    public static final String ERRORS_ATTRIBUTE_GLOBAL = "errors";
    public static final String ERROR_ATTRIBUTE_ADDRESS = "errorAddress";
    public static final String ERROR_ATTRIBUTE_EMAIL = "errorEmail";
    public static final String ERROR_ATTRIBUTE_PASSWORD = "errorPassword";

    // Attribute Auth values
    public static final String EMAIL_NOT_VALID = "fail.invalid.email";
    public static final String ACCOUNT_EXISTS = "fail.account.exists";
    public static final String INVALID_CREDENTIALS = "fail.invalid.credentials";
    public static final String PASSWORD_NOT_VALID = "fail.validation.length.password";
    public static final String NAME_NOT_VALID = "fail.blank.name";
    public static final String PASSWORDS_DO_NOT_MATCH = "fail.matches.passwords";
    public static final String USER_BLOCKED = "userBlocked";
    public static final String USER_NOT_FOUND = "fail.user.not.found";

    // Attribute Dish values
    public static final String DISH_NAME_INVALID = "fail.invalid.dish.name";
    public static final String DISH_DESCRIPTION_INVALID = "fail.invalid.dish.description";
    public static final String DISH_PRICE_INVALID = "fail.invalid.dish.price";
    public static final String ADDRESS_NOT_VALID = "fail.blank.address";

    // Attribute Order values
    public static final String ORDER_INSUFFICIENT_FUNDS = "insufficient.funds.exception";
    public static final String ORDER_WITHOUT_DISHES = "fail.order.absent";


}



