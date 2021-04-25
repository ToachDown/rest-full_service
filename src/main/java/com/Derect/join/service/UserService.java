package com.Derect.join.service;

import com.Derect.join.entity.Role;
import com.Derect.join.entity.User;
import com.Derect.join.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Value("${upload.path.avatar}")
    private String uploadPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user, String password2){
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb != null){
            return false;
        }

        if(!user.getPassword().equals(password2) && user.getPassword() != null){
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Shop. Please Visit next link : http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public void saveAvatar(User user,
                           @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            user.setFilenameAvo(resultFilename);
        }
    }

    public String updateUser(@RequestParam("file") MultipartFile file,
                           User user,
                           String password,
                           String password2,
                           String username) throws IOException {
        if(     !password.isEmpty() &&
                !password2.isEmpty() &&
                 password.compareTo(password2) == 0 &&
                 password.compareTo(user.getPassword()) != 0){
            user.setPassword(password);
        }
        if(!username.isEmpty() && username.compareTo(user.getUsername()) != 0){
            user.setUsername(username);
        }
        if(!file.isEmpty()){
            saveAvatar(user,file);
        }
        userRepository.save(user);
        return "user update!";
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if(user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }
}
