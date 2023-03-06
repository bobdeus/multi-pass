package com.bracbrun.multipass.controllers;

import com.bracbrun.multipass.models.User;
import com.bracbrun.multipass.services.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UsersController {

    private final UserService _userService;

    UsersController(UserService _userService) {
        this._userService = _userService;
    }

    @GetMapping(value = "users")
    public List<User> getUsers() {
        return _userService.getUsers();
    }

    @PostMapping(value = "saveUser")
    public User saveUser(@RequestBody @Validated User user) {
        return _userService.saveUser(user);
    }
}
