package com.exampleepaam.restaurant.mapper;

import com.exampleepaam.restaurant.model.dto.CategoryDto;
import com.exampleepaam.restaurant.model.dto.DishCreationDto;
import com.exampleepaam.restaurant.model.dto.DishResponseDto;
import com.exampleepaam.restaurant.model.entity.Category;
import com.exampleepaam.restaurant.model.entity.Dish;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for Dish and DishDTOs
 */
public class DishMapper {
    private DishMapper() {

    }

    /**
     * Maps Dish to DishResponseDTO
     *
     * @param dish Dish to be mapped
     * @return DishResponseDto
     */

    public static DishResponseDto toDishResponseDto(Dish dish) {
        return new DishResponseDto(dish.getId(), dish.getName(), dish.getDescription(),
                CategoryDto.valueOf(dish.getCategory().name()), dish.getPrice(), dish.getImageFileName());
    }

    /**
     * Maps Dish list to DishResponseDTO list
     *
     * @param dishList Dish list to be mapped
     * @return DishResponseDto list
     */
    public static List<DishResponseDto> toDishResponseDtoList(List<Dish> dishList) {
        return dishList.stream().map(DishMapper::toDishResponseDto).collect(Collectors.toList());
    }

    /**
     * Maps DishCreationDTO list to Dish
     *
     * @param dishCreationDto DishCreationDTO to be mapped
     * @return Dish
     */
    public static Dish toDish(DishCreationDto dishCreationDto) {
        return new Dish(dishCreationDto.getId(), dishCreationDto.getName(),
                dishCreationDto.getDescription(),
                Category.valueOf(dishCreationDto.getCategory().name()),
                dishCreationDto.getPrice(), dishCreationDto.getImageFileName());
    }

}

