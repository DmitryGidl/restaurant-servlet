package com.exampleepaam.restaurant.validator;

import com.exampleepaam.restaurant.constant.ErrorAttributeConstants;
import com.exampleepaam.restaurant.model.dto.OrderCreationDto;

import java.util.HashMap;
import java.util.Map;

/*
 * Class with static validation methods for OrderCreationDTO model
 */
public class OrderValidator {
    public static final int MIN_ADDRESS_LENGTH = 5;
    public static final int MAX_ADDRESS_LENGTH = 35;

    private OrderValidator() {
    }

    /**
     * Validates an order
     *
     * @param order OrderCreationDto that is validated
     * @return Map with error names and error keys if dish is invalid
     */
    public static Map<String, String> validateOrderDto(OrderCreationDto order) {
        Map<String, String> viewAttributes = new HashMap<>();
        if (!isDishOrdered(order.getDishIdQuantityMap())) {
            viewAttributes.put(ErrorAttributeConstants.ERROR_ATTRIBUTE_GLOBAL, ErrorAttributeConstants.ORDER_WITHOUT_DISHES);
        }
        if (!isAddressValid(order.getAddress())) {
            viewAttributes.put(ErrorAttributeConstants.ERROR_ATTRIBUTE_ADDRESS, ErrorAttributeConstants.ADDRESS_NOT_VALID);
        }
        return viewAttributes;

    }

    public static boolean isDishOrdered(Map<Long, Integer> orderMap) {
        if (orderMap == null) return false;
        return orderMap.values().stream().anyMatch(integer -> integer > 0);
    }

    public static boolean isAddressValid(String address) {
        return address != null &&
                !address.isBlank() &&
                (address.trim().length() >= MIN_ADDRESS_LENGTH && address.length() <= MAX_ADDRESS_LENGTH);
    }


}

