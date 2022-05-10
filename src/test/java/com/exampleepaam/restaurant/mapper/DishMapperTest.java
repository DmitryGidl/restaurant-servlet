package com.exampleepaam.restaurant.mapper;

import com.exampleepaam.restaurant.model.dto.CategoryDto;
import com.exampleepaam.restaurant.model.dto.DishResponseDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.testdata.TestData;

@ExtendWith(MockitoExtension.class)
public class DishMapperTest {



    @Test
    void toDishResponseDto_success() {
Dish dish = TestData.getDish1();
        DishResponseDto dishResponseDto = new DishResponseDto(dish.getId(), dish.getName(),
                dish.getDescription(), CategoryDto.valueOf(dish.getCategory().toString()),
                dish.getPrice(), dish.getImageFileName());
       DishResponseDto actualDishResponse = DishMapper.toDishResponseDto(dish);
        Assertions.assertEquals(dishResponseDto, actualDishResponse);
    }
}
