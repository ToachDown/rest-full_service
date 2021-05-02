package com.Derect.join.controllers;

import com.Derect.join.entity.User;
import com.Derect.join.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
