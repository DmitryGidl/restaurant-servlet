package com.exampleepaam.restaurant.mapper;

import com.exampleepaam.restaurant.model.dto.UserCreationDto;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.util.Security;

/**
 * Mapper class for User and UserDTOs
 */
public class UserMapper {
    private UserMapper() {
    }

    /**
     * Creates a new Paged object
     *
     * @param userCreationDto userCreationDTO to be mapped
     * @return User object
     */
    public static User toUser(UserCreationDto userCreationDto) {
        return new User(userCreationDto.getName(), userCreationDto.getEmail(),
                Security.hashPassword(userCreationDto.getPassword()));
    }

}

