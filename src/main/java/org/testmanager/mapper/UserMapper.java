package org.testmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.testmanager.model.UserDTO;
import org.testmanager.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "locked", ignore = true)
    User dtoToEntity(UserDTO userDTO);

    UserDTO entityToDto(User user);
}
