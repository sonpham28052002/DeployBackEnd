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
import vn.edu.iuh.fit.chat_backend.dto.VideoCall;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;
import vn.edu.iuh.fit.chat_backend.types.NotificationType;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class CallControllers {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;
    private HashMap<String, VideoCall> videoCalls = new HashMap<>();

    @MessageMapping("/inCall")
    public void inCall(@Payload String node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(node);
        String userId = rootNode.get("userId").asText();
        String roomID = rootNode.get("roomID").asText();
        String idGroup = rootNode.get("idGroup").asText();
        addNotificationMessage(userId, idGroup, "join");
        videoCalls.get(roomID).getUsers().add(User.builder().id(userId).build());
    }

    @MessageMapping("/createCall")
    public void createCall(@Payload String node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(node);
        String userId = rootNode.get("userId").asText();
        String roomID = rootNode.get("roomID").asText();
        String idGroup = rootNode.get("idGroup").asText();
        VideoCall videoCall = new VideoCall();
        videoCall.setUsers(new HashSet<>());
        videoCall.getUsers().add(User.builder().id(userId).build());
        videoCall.setRoomID(roomID);
        videoCall.setIdGroup(idGroup);
        videoCalls.put(roomID, videoCall);
        System.out.println(videoCalls.get(roomID));
    }


    @MessageMapping("/outCall")
    public void outCall(@Payload String node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(node);
        String userId = rootNode.get("userId").asText();
        String roomID = rootNode.get("roomID").asText();
        videoCalls.get(roomID).getUsers().remove(User.builder().id(userId).build());
        String idGroup = videoCalls.get(roomID).getIdGroup();
        addNotificationMessage(userId, idGroup, "leave");
        if (videoCalls.get(roomID).getUsers().size() == 0) {
            videoCalls.remove(roomID);
            updateMessageCall(userId, idGroup, roomID);
        }
    }

    public void updateMessageCall(String userId, String idGroup, String roomID) {
        List<Member> memberList = null;
        User user = userRepository.findById(userId).get();
        for (Conversation conversation : user.getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                memberList = ((ConversationGroup) conversation).getMembers();
                break;
            }
        }
        if (memberList != null) {
            for (Member member : memberList) {
                if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)) {
                    updateMessageCallForMember(member.getMember().getId(), idGroup, roomID);
                }
            }
        }
    }

    public void addNotificationMessage(String userId, String idGroup, String type) {
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setSenderDate(LocalDateTime.now());
        messageNotification.setSeen(List.of(User.builder().id(userId).build()));
        messageNotification.setMessageType(MessageType.NOTIFICATION);
        if (type.equals("join")) {
            messageNotification.setContent("đã tham gia cuộc gọi.");
            messageNotification.setNotificationType(NotificationType.JOIN_CALL);
        } else {
            messageNotification.setContent("đã rời khỏi cuộc gọi.");
            messageNotification.setNotificationType(NotificationType.OUT_CALL);
        }
        messageNotification.setSender(User.builder().id(userId).build());
        messageNotification.setReceiver(User.builder().id("group_" + idGroup).build());
        messageNotification.setId(UUID.randomUUID().toString());
        User user = userRepository.findById(userId).get();
        List<Member> members = null;
        for (Conversation conversation : user.getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().equals(idGroup.trim())) {
                members = ((ConversationGroup) conversation).getMembers();
                break;
            }
        }
        if (members != null) {
            addMessage(members, idGroup, messageNotification);
        }
    }

    public void addMessage(List<Member> members, String idGroup, MessageNotification notification) {
        for (Member member : members) {
            if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)) {
                User userMember = userRepository.findById(member.getMember().getId()).get();
                for (Conversation conversation1 : userMember.getConversation()) {
                    if (conversation1 instanceof ConversationGroup && ((ConversationGroup) conversation1).getIdGroup().equals(idGroup.trim())) {
                        conversation1.getMessages().add(notification);
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "groupChat", notification);
                        break;
                    }
                }
            }
        }
    }

    public void updateMessageCallForMember(String userId, String idGroup, String roomID) {
        User user = userRepository.findById(userId).get();
        for (Conversation conversation : user.getConversation()) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                for (int i = conversation.getMessages().size() - 1; i >= 0; i--) {
                    if ( conversation.getMessages().get(i) instanceof MessageFile && ((MessageFile) conversation.getMessages().get(i)).getUrl().indexOf(roomID) != -1) {
                        ((MessageFile) conversation.getMessages().get(i)).setUrl(null);
                        ((MessageFile) conversation.getMessages().get(i)).setTitleFile("đã kết thúc cuộc gọi.");
                        simpMessagingTemplate.convertAndSendToUser(idGroup, "updateMessage", ((MessageFile) conversation.getMessages().get(i)));
                        userRepository.save(user);
                        break;
                    }
                }
                break;
            }
        }
    }
}
