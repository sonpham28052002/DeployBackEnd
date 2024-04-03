package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.chat_backend.models.Conversation;
import vn.edu.iuh.fit.chat_backend.models.ConversationSingle;
import vn.edu.iuh.fit.chat_backend.models.Message;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/getMessageByIdSenderAndIsReceiver")
    public List<Message> getMessageByIdSenderAndIsReceiver(@RequestParam String idSender, @RequestParam String idReceiver) {
        Optional<User> sender = userRepository.findById(idSender);
        System.out.println(sender);
        Optional<User> receiver = userRepository.findById(idReceiver);
        System.out.println(receiver);

        if (sender.isEmpty() || receiver.isEmpty()){
            return new ArrayList<>();
        }
        for (Conversation conversation : sender.get().getConversation()) {
            if (conversation instanceof ConversationSingle) {
                if (((ConversationSingle) conversation).getUser().equals(receiver.get())) {
                    return conversation.getMessages();
                }
            }
        }
        return new ArrayList<>();
    }
}
