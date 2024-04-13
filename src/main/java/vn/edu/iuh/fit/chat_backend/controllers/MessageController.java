package vn.edu.iuh.fit.chat_backend.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @GetMapping("/getMessageByIdSenderAndIsReceiver")
    public List<Message> getMessageByIdSenderAndIsReceiver(@RequestParam String idSender, @RequestParam String idReceiver) {
        Optional<User> sender = userRepository.findById(idSender);
        Optional<User> receiver = userRepository.findById(idReceiver);
        if (sender.isEmpty() || receiver.isEmpty()) {
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
    @GetMapping("/getMessageAndMemberByIdSenderAndIdGroup")
    public List<Message> getMessageAndMemberByIdSenderAndIdGroup(@RequestParam String idSender, @RequestParam String idGroup) {
        Optional<User> sender = userRepository.findById(idSender);
        if (sender.isEmpty()) {
            return new ArrayList<>();
        }
        for (Conversation conversation : sender.get().getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().equals(idGroup)) {
                return conversation.getMessages();
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("/getMemberByIdSenderAndIdGroup")
    public List<Member> getMemberByIdSenderAndIdGroup(@RequestParam String idSender, @RequestParam String idGroup) {
        Optional<User> sender = userRepository.findById(idSender);
        if (sender.isEmpty()) {
            return new ArrayList<>();
        }
        for (Conversation conversation : sender.get().getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().equals(idGroup)) {
                for (Member member:((ConversationGroup) conversation).getMembers()) {
                    User user = userRepository.findById(member.getMember().getId()).get();
                    member.setMember(user);
                }
                return ((ConversationGroup) conversation).getMembers();
            }
        }
        return new ArrayList<>();
    }

}