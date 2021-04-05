package com.Derect.join.controllers;

import com.Derect.join.entity.Role;
import com.Derect.join.entity.User;
import com.Derect.join.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String password2,
            User user, Model model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        boolean isConfirmEmpty = StringUtils.isEmpty(password2);

        if(isConfirmEmpty){
            return "registration";
        }

        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        if(!user.getPassword().equals(password2) && user.getPassword() != null){
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
