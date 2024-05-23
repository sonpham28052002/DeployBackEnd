package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;
import vn.edu.iuh.fit.chat_backend.types.NotificationType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class MessageNotificationService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserRepository userRepository;

    public void sendNotification(String content, String idSender, String idReceiver, String idGroup, NotificationType type) {
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setId(UUID.randomUUID().toString());
        messageNotification.setNotificationType(type);
        messageNotification.setMessageType(MessageType.NOTIFICATION);
        messageNotification.setContent(content);
        messageNotification.setSeen(new HashSet<>());
        messageNotification.setSenderDate(LocalDateTime.now());
        messageNotification.setSender(User.builder().id(idSender).build());
        if (!idReceiver.equals("")) {
            messageNotification.setUser(User.builder().id(idReceiver).build());
        }
        messageNotification.setReceiver(User.builder().id("group_" + idGroup).build());
        insertMessageNotification(messageNotification, idGroup, idSender);
    }


    public MessageNotification createNotification(String content, String idSender, String idReceiver, String idGroup, NotificationType type) {
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setId(UUID.randomUUID().toString());
        messageNotification.setNotificationType(type);
        messageNotification.setMessageType(MessageType.NOTIFICATION);
        messageNotification.setContent(content);
        messageNotification.setSeen(new HashSet<>());
        messageNotification.setSenderDate(LocalDateTime.now());
        messageNotification.setSender(User.builder().id(idSender).build());
        if (!idReceiver.equals("")) {
            messageNotification.setUser(User.builder().id(idReceiver).build());
        }
        messageNotification.setReceiver(User.builder().id("group_" + idGroup).build());
        return messageNotification;
    }

    public void insertMessageNotification(MessageNotification notification, String idGroup, String idSender) {
        ConversationGroup group = null;
        User sender = userRepository.findById(idSender).get();
        for (Conversation conversation : sender.getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                group = (ConversationGroup) conversation;
                break;
            }
        }
        for (Member member : group.getMembers()) {
            User user = userRepository.findById(member.getMember().getId()).get();
            if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)) {
                for (Conversation conversation : user.getConversation()) {
                    if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                        conversation.getMessages().add(notification);
                        conversation.setLastMessage();
                        userRepository.save(user);
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "groupChat", notification);
                        break;
                    }
                }
            }
        }
    }
    public void insertMessageNotificationV2(MessageNotification notification, ConversationGroup group) {
        for (Member member : group.getMembers()) {
            User user = userRepository.findById(member.getMember().getId()).get();
            if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)) {
                for (Conversation conversation : user.getConversation()) {
                    if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(group.getIdGroup().trim())) {
                        conversation.getMessages().add(notification);
                        conversation.setLastMessage();
                        userRepository.save(user);
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "/groupChat", notification);
                        break;
                    }
                }
            }
        }
    }
    public void insertListMessageNotification(List<MessageNotification> notificationList, String idGroup, String idSender){
        ConversationGroup group = null;
        User sender = userRepository.findById(idSender).get();
        for (Conversation conversation : sender.getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                group = (ConversationGroup) conversation;
                break;
            }
        }
        for (Member member : group.getMembers()) {
            User user = userRepository.findById(member.getMember().getId()).get();
            if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)) {
                for (Conversation conversation : user.getConversation()) {
                    if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                        conversation.getMessages().addAll(notificationList);
                        conversation.setLastMessage();
                        userRepository.save(user);
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "ChangeRoleNotification", notificationList);
                        break;
                    }
                }
            }
        }
    }
}
