package org.testmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.testmanager.model.UserDTO;
import org.testmanager.model.UserListDTO;
import org.testmanager.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/freeuser")
    UserDTO getFreeUser() {
        return userService.getFreeUser();
    }

    @GetMapping(value = "/users")
    UserListDTO getUsers(@RequestParam(value = "page", defaultValue = "0") @Min(0L) int page,
                         @RequestParam(value = "size", defaultValue = "50") @Min(1L) @Max(100L) int size) {
        return userService.getUsersPaginated(page, size);
    }

    @PostMapping("/users")
    UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

}
