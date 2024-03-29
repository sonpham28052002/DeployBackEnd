package vn.edu.iuh.fit.chat_backend.controllers;

import com.azure.core.annotation.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.Conversation;
import vn.edu.iuh.fit.chat_backend.models.ConversationSingle;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.*;

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

        Optional<User> user = userRepository.findById(id);
        Collections.sort(user.get().getConversation(), new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                return o2.getUpdateLast().compareTo(o1.getUpdateLast());
            }
        });
        System.out.println("aaaaa");
        for (Conversation conversation:user.get().getConversation()) {
            System.out.println(conversation.getUpdateLast() +" "+((ConversationSingle) conversation).getUser().getUserName());
        }
        return user;
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

    @PostMapping("/insertUsers")
    public User insertUsers(@RequestBody User user){
        return userRepository.save(user);
    }
    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user){
        Optional<User> user1 = userRepository.findById(user.getId());
        if (user1.isPresent()){
            user.setConversation(user1.get().getConversation());
            user.setFriendList(user1.get().getFriendList());
        }
        return userRepository.save(user);
    }

}
