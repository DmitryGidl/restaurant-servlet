package com.exampleepaam.restaurant.validator;

import com.exampleepaam.restaurant.constant.ErrorAttributeConstants;
import com.exampleepaam.restaurant.model.dto.DishCreationDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/*
 * Class with static validation methods for DishCreationDTO model
 */
public class DishValidator {

    private DishValidator() {
    }

    private static final int MAX_NAME_LENGTH = 30;
    private static final int MIN_NAME_LENGTH = 4;

    private static final int MAX_DESCRIPTION_LENGTH = 40;
    private static final int MIN_DESCRIPTION_LENGTH = 4;

    /**
     * Validates a dish
     *
     * @param dish DishCreationDTO that is validated
     * @return Map with error names and error keys if dish is invalid
     */
    public static Map<String, String> validateDish(DishCreationDto dish) {
        Map<String, String> viewAttributes = new HashMap<>();

        if (!isNameValid(dish.getName())) viewAttributes.put(
                ErrorAttributeConstants.ERROR_ATTRIBUTE_NAME, ErrorAttributeConstants.DISH_NAME_INVALID);

        if (!isDescriptionValid(dish.getDescription())) viewAttributes.put(
                ErrorAttributeConstants.ERROR_ATTRIBUTE_DESCRIPTION, ErrorAttributeConstants.DISH_DESCRIPTION_INVALID);

        if (!isPriceValid(dish.getPrice())) viewAttributes.put(
                ErrorAttributeConstants.ERROR_ATTRIBUTE_PRICE, ErrorAttributeConstants.DISH_PRICE_INVALID);

        return viewAttributes;
    }


    public static boolean isNameValid(String name) {
        if (name == null || name.isBlank()) return false;
        return name.trim().length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH;
    }

    public static boolean isDescriptionValid(String description) {
        if (description == null || description.isBlank()) return false;
        return description.trim().length() >= MIN_DESCRIPTION_LENGTH && description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    public static boolean isPriceValid(BigDecimal price) {
        return price != null && (price.compareTo(BigDecimal.ZERO) >= 0);
    }

}
