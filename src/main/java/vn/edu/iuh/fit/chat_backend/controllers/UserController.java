package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/getUserById")
    public Optional<User> getUserById(@RequestParam String id) {
        return userRepository.findById(id);
    }

    @GetMapping("/getInfoUserById")
    public Optional<User> getInfoUserById(@RequestParam String id) {
         Optional<User> user= userRepository.findById(id);
         user.get().setConversation(null);
         user.get().setFriendList(null);
        return user;
    }

    @PostMapping("/insertUser")
    public User insertUser(@RequestBody User user){
        user.setConversation(new ArrayList<>());
        user.setFriendList(new ArrayList<>());
        return userRepository.save(user);
    }
    @PostMapping("/updateUser")
    public User updateUser(@RequestBody User user){
        user.setConversation(new ArrayList<>());
        user.setFriendList(new ArrayList<>());
        return userRepository.save(user);
    }

}
