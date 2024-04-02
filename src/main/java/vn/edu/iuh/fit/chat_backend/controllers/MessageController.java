package vn.edu.iuh.fit.chat_backend.controllers;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.dtos.MessageDto;
import vn.edu.iuh.fit.chat_backend.models.Message;
import vn.edu.iuh.fit.chat_backend.models.MessageFile;
import vn.edu.iuh.fit.chat_backend.models.MessageText;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.MessageRepository;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @GetMapping("/getMessageById")
    public Optional<Message> getMessageById(@RequestParam String id) {

        Optional<Message> message = messageRepository.findById(id);

        return message;
    }

    @GetMapping("/getMessageBySender")
    public List<Message> getMessageBySender(@RequestParam String id) {

        List<Message> messagesBySender = messageRepository.findAllBySender(userRepository.findById(id).get());

        return messagesBySender;
    }

    @PostMapping("/insertMessage")
    public Message insertUser(@RequestBody MessageDto dto) {
        User sender = userRepository.findById(dto.getSender().getId()).get();
        User receiver = userRepository.findById(dto.getReceiver().getId()).get();
        if(dto.getContent() != null){ // text
            MessageText messageText = new MessageText();
            messageText.setId(dto.getId());
            messageText.setMessageType(dto.getMessageType());
            messageText.setSender(sender);
            messageText.setReceiver(receiver);
            messageText.setContent(dto.getContent());
            return messageRepository.save(messageText);
        }
        MessageFile messageFile = new MessageFile();
        messageFile.setId(dto.getId());
        messageFile.setMessageType(dto.getMessageType());
        messageFile.setSender(sender);
        messageFile.setReceiver(receiver);
        messageFile.setSize(dto.getSize());
        messageFile.setTitleFile(dto.getTitleFile());
        messageFile.setUrl(dto.getUrl());
        return messageRepository.save(messageFile);
    }
}
