package vn.edu.iuh.fit.chat_backend.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.types.MemberType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
                    List<Message> messageList = conversation.getMessages();
                    if (messageList.get(messageList.size()-1).getSeen().contains(User.builder().id(idSender).build())){
                        return conversation.getMessages();
                    }else{
                        conversation.getMessages().forEach(message -> {
                            Set <User> seen = message.getSeen();
                            seen.add(User.builder().id(idSender).build());
                            message.setSeen(seen);
                        });
                        conversation.setLastMessage();
                        userRepository.save(sender.get());
                        simpMessagingTemplate.convertAndSendToUser(idSender, "/SeenMessageSingle", conversation.getMessages());
                        updateSeenMessageSingleReceiver(idSender, idReceiver);
                        return conversation.getMessages();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public void updateSeenMessageSingleReceiver(String idUser , String idOwner){
        Optional<User> sender = userRepository.findById(idOwner);
        Optional<User> receiver = userRepository.findById(idUser);

        for (Conversation conversation : sender.get().getConversation()) {
            if (conversation instanceof ConversationSingle) {
                if (((ConversationSingle) conversation).getUser().equals(receiver.get())) {
                    List<Message> messageList = conversation.getMessages();
                    if (!messageList.get(messageList.size()-1).getSeen().contains(User.builder().id(idUser).build())){
                        conversation.getMessages().forEach(message -> {
                            Set<User> seen = message.getSeen();
                            seen.add(User.builder().id(idUser).build());
                            message.setSeen(seen);
                        });
                        conversation.setLastMessage();
                        userRepository.save(sender.get());
                        simpMessagingTemplate.convertAndSendToUser(idOwner, "/SeenMessageSingle", conversation.getMessages());
                    }
                }
            }
        }
    }


    @GetMapping("/getMessageAndMemberByIdSenderAndIdGroup")
    public List<Message> getMessageAndMemberByIdSenderAndIdGroup(@RequestParam String idSender, @RequestParam String idGroup) {
        Optional<User> sender = userRepository.findById(idSender);
        if (sender.isEmpty()) {
            return new ArrayList<>();
        }
        for (Conversation conversation : sender.get().getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().equals(idGroup)) {
                if (conversation.getMessages().get(conversation.getMessages().size()-1).getSeen().contains(User.builder().id(idSender).build())){
                    return conversation.getMessages();
                }else{
                    return updateSeenMessageGroup(idGroup,((ConversationGroup) conversation).getMembers(),idSender);
                }
            }
        }
        return new ArrayList<>();
    }

    public List<Message> updateSeenMessageGroup(String idGroup, List<Member> memberList, String idSender){
        List<Message> messageListSender = new ArrayList<>();
        for (Member member:memberList) {
            if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)){
                User user = userRepository.findById(member.getMember().getId()).get();
                for (Conversation conversation:user.getConversation()) {
                    if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())){
                        conversation.getMessages().forEach(message -> {
                            Set<User> seen = message.getSeen();
                            seen.add(User.builder().id(idSender).build());
                            message.setSeen(seen);
                        });
                        if (user.getId().trim().equals(idSender.trim())){
                            messageListSender = conversation.getMessages();
                        }
                        simpMessagingTemplate.convertAndSendToUser(user.getId(), "/SeenMessageGroup", conversation.getMessages());
                        userRepository.save(user);
                    }
                }
            }
        }
        return messageListSender;
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