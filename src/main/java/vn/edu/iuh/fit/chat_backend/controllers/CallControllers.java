package vn.edu.iuh.fit.chat_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import vn.edu.iuh.fit.chat_backend.models.User;

import java.util.HashSet;
import java.util.Set;

@Controller
public class CallControllers {
    private Set<User> litUserOnCall = new HashSet<>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/checkSingleCall")
    public void checkUserContain(@Payload String node) throws JsonProcessingException {
        System.out.println(node);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(node);
        String ownerId = rootNode.get("ownerId").asText();
        String userId = rootNode.get("userId").asText();
        String addressReceiver = rootNode.get("addressReceiver").asText();
        if (litUserOnCall.contains(User.builder().id(ownerId).build())) {
            simpMessagingTemplate.convertAndSendToUser(addressReceiver, "/checkSingleCallResult", true);
            return;
        } else {
            if (litUserOnCall.contains(User.builder().id(userId).build())) {

            } else {
                litUserOnCall.add(User.builder().id(ownerId).build());
                litUserOnCall.add(User.builder().id(userId).build());
            }
            simpMessagingTemplate.convertAndSendToUser(addressReceiver, "/checkSingleCallResult", false);
        }
    }

    @MessageMapping("/outCall")
    public void outUserContain(@Payload String node) throws JsonProcessingException {
        System.out.println(node);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(node);
        String userId = rootNode.get("userId").asText();
        String addressReceiver = rootNode.get("addressReceiver").asText();
        if (litUserOnCall.contains(User.builder().id(userId).build())) {
            litUserOnCall.remove(User.builder().id(userId).build());
            simpMessagingTemplate.convertAndSendToUser(addressReceiver, "/outCall", false);
        }
    }
}
