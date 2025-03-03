package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.DTO.RegisterDTO;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO.getUsername(), registerDTO.getPassword(), registerDTO.getEmail());
    }
}
