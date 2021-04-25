package com.Derect.join.controllers;

import com.Derect.join.entity.User;
import com.Derect.join.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(@AuthenticationPrincipal User user,
                               Model model) {
        model.addAttribute("isAuthorized", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String password2,
            User user, Model model) {
        boolean isConfirmEmpty = StringUtils.isEmpty(password2);

        if(isConfirmEmpty){
            return "registration";
        }

        if(!userService.addUser(user,password2)){
            model.addAttribute("message", "User exists");
            return "registration";
        }

        return "redirect:/login";
    }

    @PostMapping("/product/page/{id}")
    public String editUser(Model model,
                           @AuthenticationPrincipal User user,
                           @RequestParam("username") String username,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "password2", required = false) String password2,
                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        userService.updateUser(file, user, password, password2,username);
        return "redirect:/product/page/" + user.getId();
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model,
                           @AuthenticationPrincipal User user,
                           @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(isActivated){
            model.addAttribute("message", "user successfully activated");
        } else {
            model.addAttribute("message", "Activation code not found");
        }
        model.addAttribute("isAuthorized", user);
        return "login";
    }
}
