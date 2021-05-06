package com.Derect.join.controllers;

import com.Derect.join.entity.Role;
import com.Derect.join.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping
    public String homPage(@AuthenticationPrincipal User user,
                          Model model) {
        if(user != null) {
            model.addAttribute("name", user.getUsername());
            model.addAttribute("isAuthorized", user);
            model.addAttribute("ADMIN",Role.ADMIN);
        } else{
            model.addAttribute("name", "Guest");
            model.addAttribute("ADMIN",Role.ADMIN);
        }
        return "homePage";
    }
}
