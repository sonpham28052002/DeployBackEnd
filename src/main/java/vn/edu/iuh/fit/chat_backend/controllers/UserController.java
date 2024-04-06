package vn.edu.iuh.fit.chat_backend.controllers;

import com.azure.core.annotation.Put;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.*;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
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
        for (Conversation conversation:user.get().getConversation()) {
            if (conversation instanceof ConversationSingle){
                String idUserConversation = ((ConversationSingle) conversation).getUser().getId();
                User userConversation =userRepository.findById(idUserConversation).get();
                ((ConversationSingle) conversation).setUser(userConversation);
            }
        }
        System.out.println(user.get().getFriendList().size());
        for (Friend friend:user.get().getFriendList()) {
            User user1 = userRepository.findById(friend.getUser().getId()).get();
            friend.setUser(user1);
        }
        return user;
    }

    @GetMapping("/getInfoUserById")
    public Optional<User> getInfoUserById(@RequestParam String id) {
        Optional<User> user = userRepository.findById(id);
        user.get().setConversation(null);
        user.get().setFriendList(null);
        return user;
    }

    @GetMapping("/getInfoUserInGroupById")
    public List<User> getInfoUserInGroupById(@RequestBody List<User> idUsersGroup) {
        List<User> userList = new ArrayList<>();
        for (User user : idUsersGroup) {
            User rs = userRepository.findById(user.getId()).get();
            userList.add(User.builder().id(rs.getId()).avt(rs.getAvt()).userName(rs.getUserName()).build());
        }
        return userList;
    }

    @GetMapping("/getMessageByIdSenderAndIsReceiver")
    public List<Message> getMessageByIdSenderAndIsReceiver(@RequestParam String idSender, @RequestParam String idReceiver) {
        User sender = userRepository.findById(idSender).get();
        User receiver = userRepository.findById(idReceiver).get();
        for (Conversation conversation : sender.getConversation()) {
            if (conversation instanceof ConversationSingle) {
                if (((ConversationSingle) conversation).getUser().equals(receiver)) {
                    return conversation.getMessages();
                }
            }
        }
        return null;
    }


    @PostMapping("/insertUser")
    public User insertUser(@RequestBody User user) {
        Faker faker = new Faker();
        user.setAvt(faker.avatar().image());
        user.setCoverImage(faker.internet().image());
        user.setConversation(new ArrayList<>());
        user.setFriendList(new ArrayList<>());
        return userRepository.save(user);
    }

    @DeleteMapping("/deleteUserById")
    public boolean deleteUserById(@RequestParam String id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        Optional<User> user1 = userRepository.findById(user.getId());
        if (user1.isPresent()) {
            user.setConversation(user1.get().getConversation());
            user.setFriendList(user1.get().getFriendList());
        }
        return userRepository.save(user);
    }

}
