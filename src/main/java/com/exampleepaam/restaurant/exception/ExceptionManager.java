package com.exampleepaam.restaurant.exception;

/**
 * Helper class for getting exceptions
 */
public class ExceptionManager {

    private ExceptionManager() {
    }


    public static UserAlreadyExistsException getUserAlreadyExistsException(String email) {
        String eMessage = String
                .format("User tried to register with email %s, but it already exists",
                        email);
        return new UserAlreadyExistsException(eMessage);
    }


    public static UnauthorizedActionException getUnauthorizedActionException(String eMessage) {
        return new UnauthorizedActionException(eMessage);
    }
}
