package org.testmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testmanager.model.UserDto;
import org.testmanager.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/freeuser")
    UserDto getFreeUser() {
        return userService.getFreeUser();
    }

}
