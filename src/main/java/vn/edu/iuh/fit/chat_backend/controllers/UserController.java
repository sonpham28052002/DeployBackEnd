package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/getUserById")
    public User getUserById(@RequestParam String id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/getInfoUserById")
    public User getInfoUserById(@RequestParam String id) {
         User user= userRepository.findById(id).get();
         user.setConversation(null);
         user.setFriendList(null);
        return user;
    }

}
