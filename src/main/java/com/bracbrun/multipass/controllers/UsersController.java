package com.bracbrun.multipass.controllers;

import com.bracbrun.multipass.models.User;
import com.bracbrun.multipass.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
