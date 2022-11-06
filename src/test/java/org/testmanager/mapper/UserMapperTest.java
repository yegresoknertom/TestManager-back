package org.testmanager.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.testmanager.model.UserDTO;
import org.testmanager.model.entity.User;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userToUserDTOTest() {
        var user = new User();
        user.setLogin("TestLogin");
        user.setPassword("Password");
        user.setLocked(true);

        var result = mapper.entityToDto(user);

        assertAll(
                () -> assertEquals(user.getLogin(), result.getLogin(), "logins do not match"),
                () -> assertEquals(user.getPassword(), result.getPassword(), "Passwords do not match"),
                () -> assertTrue(user.isLocked(), "User should be locked")
        );

    }

    @Test
    void userDTOToUserTest() {
        var user = new UserDTO()
                .setLogin("TestLogin")
                .setPassword("Password")
                .setLocked(false);

        var result = mapper.dtoToEntity(user);

        assertAll(
                () -> assertEquals(user.getLogin(), result.getLogin(), "logins do not match"),
                () -> assertEquals(user.getPassword(), result.getPassword(), "Passwords do not match"),
                () -> assertTrue(!user.isLocked(), "User should not be locked")
        );

    }

}
