package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.chat_backend.models.Message;
import vn.edu.iuh.fit.chat_backend.models.MessageFile;
import vn.edu.iuh.fit.chat_backend.models.MessageText;
import vn.edu.iuh.fit.chat_backend.models.SendQR;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

@RestController
public class ChatService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/private-single-message")
    public Message recMessageTextSingle(@Payload MessageText messageText, @Payload MessageFile messageFile) {
        if (messageText.getContent() == null) {
            messageService.insertMessageSingleSender(messageFile);
            messageService.insertMessageSingleReceiver(messageFile);
            simpMessagingTemplate.convertAndSendToUser(messageFile.getReceiver().getId() + messageFile.getSender().getId(), "/singleChat", messageFile);
            return messageFile;
        } else {
            messageService.insertMessageSingleSender(messageText);
            messageService.insertMessageSingleReceiver(messageText);
            simpMessagingTemplate.convertAndSendToUser(messageText.getReceiver().getId() + messageText.getSender().getId(), "/singleChat", messageText);
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