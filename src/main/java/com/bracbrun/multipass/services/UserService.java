package com.bracbrun.multipass.services;

import com.bracbrun.multipass.models.User;
import com.bracbrun.multipass.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository _userRepository;

    UserService(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    public List<User> getUsers() {
        return _userRepository.findAll();
    }

    public User saveUser(User user) {
        return _userRepository.save(user);
    }
}
