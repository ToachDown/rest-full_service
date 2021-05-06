package com.Derect.join.controllers;

import com.Derect.join.entity.Role;
import com.Derect.join.entity.User;
import com.Derect.join.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(@AuthenticationPrincipal User user,
                           Model model){
        model.addAttribute("isAuthorized",user);
        model.addAttribute("ADMIN",Role.ADMIN);
        model.addAttribute("usersList", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditRole(@PathVariable User user,
                               Model model){
        ArrayList<String> newRoles = new ArrayList<>();
        model.addAttribute("roles", Role.values());
        model.addAttribute("newRoles", newRoles);
        model.addAttribute("ADMIN",Role.ADMIN);
        model.addAttribute("USER",user);
        model.addAttribute("isAuthorized",user);
        return "userEdit";
    }

    @PostMapping
    public String userSave(@RequestParam("userID") User user,
                           @RequestParam Map<String, String> form,
                           @RequestParam String username){
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                        .map(Role::name)
                        .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
        return "redirect:/user";
    }
}
