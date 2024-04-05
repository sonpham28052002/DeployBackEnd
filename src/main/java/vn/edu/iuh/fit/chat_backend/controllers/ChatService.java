package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import vn.edu.iuh.fit.chat_backend.models.Message;
import vn.edu.iuh.fit.chat_backend.models.MessageFile;
import vn.edu.iuh.fit.chat_backend.models.MessageText;
import vn.edu.iuh.fit.chat_backend.models.SendQR;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;

@Controller
public class ChatService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/react-message")
    public String reactMessage(@Payload MessageText messageText, @Payload MessageFile messageFile){
        if (messageText.getContent() == null) {
            System.out.println(messageFile.getReact());
            simpMessagingTemplate.convertAndSendToUser(messageFile.getId(), "react-message",messageFile.getReact());
        }else {
            System.out.println(messageText.getReact());
        }
        return "";
    }

    @MessageMapping("/retrieve-message")
    public String retrieveMessage(@Payload MessageText messageText, @Payload MessageFile messageFile){
        if (messageText.getContent() == null) {
            System.out.println(messageFile);
            Message messageNew =messageService.retrieveMessageSingle(messageFile);
            simpMessagingTemplate.convertAndSendToUser(messageFile.getReceiver().getId(), "/retrieveMessage",messageNew);
            simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId(), "/retrieveMessage",messageNew);
        }else {
            System.out.println(messageText);
             Message messageNew =messageService.retrieveMessageSingle(messageText);
            simpMessagingTemplate.convertAndSendToUser(messageText.getReceiver().getId(), "/retrieveMessage",messageNew);
            simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId(), "/retrieveMessage",messageNew);
        }
        return "";
    }
    @MessageMapping("/private-single-message")
    public Message recMessageTextSingle(@Payload MessageText messageText, @Payload MessageFile messageFile) {
        if (messageText.getContent() == null) {
            messageFile.setSenderDate(LocalDateTime.now());
            messageService.insertMessageSingleSender(messageFile);
            messageService.insertMessageSingleReceiver(messageFile);
            simpMessagingTemplate.convertAndSendToUser(messageFile.getReceiver().getId()+"", "/singleChat", messageFile);
            simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId()+"", "/singleChat", messageFile);

            return messageFile;
        } else {
            messageText.setSenderDate(LocalDateTime.now());
            messageService.insertMessageSingleSender(messageText);
            messageService.insertMessageSingleReceiver(messageText);
            simpMessagingTemplate.convertAndSendToUser(messageText.getReceiver().getId()+"", "/singleChat", messageText);
            simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId()+"", "/singleChat", messageText);
            return messageText;
        }

    }

    @MessageMapping("/private-group-message")
    public Message recMessageGroup(@Payload Message message) {
//        simpMessagingTemplate.convertAndSendToUser(message.getSender().getId(), "/groupChat", message);
        return message;
    }

    @MessageMapping("QR")
    public SendQR recMessageQR(@Payload SendQR sendQR) {
        simpMessagingTemplate.convertAndSendToUser(sendQR.getIp(), "/QR", sendQR);
        System.out.println(sendQR);
        return sendQR;
    }

}
