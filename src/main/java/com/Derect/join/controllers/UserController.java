package com.Derect.join.controllers;

import com.Derect.join.entity.Role;
import com.Derect.join.entity.User;
import com.Derect.join.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("isAuthorized",user);
        model.addAttribute("usersList", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditRole(@PathVariable User user,
                               Model model){
        model.addAttribute("roles", Role.values());
        model.addAttribute("USER",user);
        model.addAttribute("isAuthorized",user);
        return "userEdit";
    }

    @PostMapping
    public String userSave(@RequestParam("userID") User user,
                           @RequestParam String username){
        user.setUsername(username);

        userRepository.save(user);
        return "redirect:/user";
    }
}
