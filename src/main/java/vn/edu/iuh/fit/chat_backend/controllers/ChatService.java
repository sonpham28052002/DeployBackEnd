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
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.services.UserService;
import vn.edu.iuh.fit.chat_backend.types.GroupStatus;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ChatService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @MessageMapping("/react-message")
    public String reactMessage(@Payload MessageText messageText, @Payload MessageFile messageFile) {
        if (messageText.getContent() == null) {
            System.out.println(messageFile.getReact());
            simpMessagingTemplate.convertAndSendToUser(messageFile.getId(), "react-message", messageFile.getReact());
        } else {
            System.out.println(messageText.getReact());
        }
        return "";
    }

    @MessageMapping("/retrieve-message")
    public String retrieveMessage(@Payload MessageText messageText, @Payload MessageFile messageFile) {
        if (messageText.getContent() == null) {
            int index = messageFile.getReceiver().getId().indexOf("_");
            if (index == -1) {
                System.out.println(messageFile);
                Message messageNew = messageService.retrieveMessageSingle(messageFile);
                simpMessagingTemplate.convertAndSendToUser(messageFile.getReceiver().getId(), "/retrieveMessage", messageNew);
                simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId(), "/retrieveMessage", messageNew);
            } else {
                String idGroup = messageFile.getReceiver().getId().substring(messageFile.getReceiver().getId().indexOf("_") + 1, messageFile.getReceiver().getId().length());
                List<Member> memberList = messageService.retrieveMessageGroup(messageFile, idGroup);
                if (memberList.size() != 0) {
                    for (Member member : memberList) {
                        messageFile.setMessageType(MessageType.RETRIEVE);
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId().trim(), "/retrieveMessage", messageFile);
                    }
                } else {
                    simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId().trim(), "/retrieveMessage", messageFile);
                }
            }
        } else {
            int index = messageText.getReceiver().getId().indexOf("_");
            if (index == -1) {
                System.out.println(messageText);
                Message messageNew = messageService.retrieveMessageSingle(messageText);
                simpMessagingTemplate.convertAndSendToUser(messageText.getReceiver().getId(), "/retrieveMessage", messageNew);
                simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId(), "/retrieveMessage", messageNew);
            } else {
                String idGroup = messageText.getReceiver().getId().substring(messageText.getReceiver().getId().indexOf("_") + 1, messageText.getReceiver().getId().length());
                List<Member> memberList = messageService.retrieveMessageGroup(messageText, idGroup);
                if (memberList.size() != 0) {
                    for (Member member : memberList) {
                        messageText.setMessageType(MessageType.RETRIEVE);
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId().trim(), "/retrieveMessage", messageText);
                    }
                } else {
                    simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId().trim(), "/retrieveMessage", messageText);
                }
            }

        }
        return "";
    }

    @MessageMapping("/delete-message")
    public String deleteMessage(@Payload MessageText messageText, @Payload MessageFile messageFile, @Payload String idGroup) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(idGroup);
        String Group = rootNode.get("idGroup").asText();
        String ownerId = rootNode.get("ownerId").asText();
        if (Group.trim().equals("")) {
            if (messageText.getContent() == null) {
                System.out.println(messageFile);
                messageService.deleteMessageSingle(messageFile, ownerId, Group);
                simpMessagingTemplate.convertAndSendToUser(ownerId, "/deleteMessage", messageFile);
            } else {
                System.out.println(messageText.getReceiver());
                messageService.deleteMessageSingle(messageText, ownerId, Group);
                simpMessagingTemplate.convertAndSendToUser(ownerId, "/deleteMessage", messageText);
            }
        } else {
            if (messageText.getContent() == null) {
                System.out.println(messageFile);
                messageService.deleteMessageSingle(messageFile, ownerId, Group);
                simpMessagingTemplate.convertAndSendToUser(ownerId, "/deleteMessage", messageFile);
            } else {
                System.out.println(messageText.getReceiver());
                messageService.deleteMessageSingle(messageText, ownerId, Group);
                simpMessagingTemplate.convertAndSendToUser(ownerId, "/deleteMessage", messageText);
            }
        }
        return "";
    }

    @MessageMapping("/forward-message")
    public Message forwardMessage(@Payload List<MessageText> messageTexts, @Payload List<MessageFile> messageFiles) {

        if (messageTexts.get(0).getContent() == null) {
            for (MessageFile messageFile : messageFiles) {
                int index = messageFile.getReceiver().getId().indexOf("_");
                if (index == -1) {
                    messageFile.setSenderDate(LocalDateTime.now());
                    messageFile.setId(UUID.randomUUID().toString());
                    messageService.insertMessageSingleSender(messageFile);
                    messageService.insertMessageSingleReceiver(messageFile);
                    simpMessagingTemplate.convertAndSendToUser(messageFile.getReceiver().getId() + "", "/singleChat", messageFile);
                    simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId() + "", "/singleChat", messageFile);
                } else {
                    String idGroup = messageFile.getReceiver().getId().substring(messageFile.getReceiver().getId().indexOf("_") + 1, messageFile.getReceiver().getId().length());
                    List<Member> memberList = messageService.insertMessageGroup(messageFile, idGroup);
                    if (memberList.size() != 0) {
                        for (Member member : memberList) {
                            messageFile.setId(UUID.randomUUID().toString());
                            simpMessagingTemplate.convertAndSendToUser(member.getMember().getId() + "", "/groupChat", messageFile);
                        }
                    } else {
                        simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId() + "", "/groupChat", messageFile);
                    }
                }
            }
            return null;
        } else {

            for (MessageText messageText : messageTexts) {
                System.out.println("user: " + messageText.getReceiver().getId());
                int index = messageText.getReceiver().getId().indexOf("_");
                if (index == -1) {
                    messageText.setSenderDate(LocalDateTime.now());
                    messageText.setId(UUID.randomUUID().toString());
                    messageService.insertMessageSingleSender(messageText);
                    messageService.insertMessageSingleReceiver(messageText);
                    simpMessagingTemplate.convertAndSendToUser(messageText.getReceiver().getId() + "", "/singleChat", messageText);
                    simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId() + "", "/singleChat", messageText);
                } else {
                    String idGroup = messageText.getReceiver().getId().substring(messageText.getReceiver().getId().indexOf("_") + 1, messageText.getReceiver().getId().length());
                    List<Member> memberList = messageService.insertMessageGroup(messageText, idGroup);
                    if (memberList.size() != 0) {
                        for (Member member : memberList) {
                            messageText.setId(UUID.randomUUID().toString());
                            simpMessagingTemplate.convertAndSendToUser(member.getMember().getId() + "", "/groupChat", messageText);
                        }
                    } else {
                        simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId() + "", "/groupChat", messageText);
                    }
                }
            }
            return null;
        }
    }

    @MessageMapping("/create-Group")
    public void createGroup(@Payload List<User> members) {

    }

    @MessageMapping("/deleteConversation")
    public Conversation deleteConversation(@Payload String conversation) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(conversation);
        String idGroup = rootNode.get("idGroup").asText();
        String idUser = rootNode.get("idUser").asText();
        String ownerId = rootNode.get("ownerId").asText();
        Conversation conversation1 = messageService.deleteConversation(ownerId, idUser, idGroup);
        if (conversation1 != null) {
            simpMessagingTemplate.convertAndSendToUser(ownerId + "", "/deleteConversation", conversation1);
        } else {
            simpMessagingTemplate.convertAndSendToUser(ownerId + "", "/deleteConversation", new Conversation());
        }
        return null;
    }

    @MessageMapping("/private-single-message")
    public Message recMessageTextSingle(@Payload MessageText messageText, @Payload MessageFile messageFile, @Payload String idGroup) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(idGroup);
        String idG = rootNode.get("idGroup").asText();
        if (!idG.trim().equals("")) {
            System.out.println(idG);
            if (messageText.getContent() == null) {
                List<Member> memberList = messageService.insertMessageGroup(messageFile, idG);
                if (memberList.size() != 0) {
                    for (Member member : memberList) {
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId() + "", "/groupChat", idGroup);
                    }
                } else {
                    simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId() + "", "/groupChat", idGroup);

                }
            } else {
                List<Member> memberList = messageService.insertMessageGroup(messageText, idG);
                if (memberList.size() != 0) {
                    for (Member member : memberList) {
                        simpMessagingTemplate.convertAndSendToUser(member.getMember().getId() + "", "/groupChat", idGroup);
                    }
                } else {
                    simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId() + "", "/groupChat", idGroup);
                }
            }
        } else {
            if (messageText.getContent() == null) {
                messageFile.setSenderDate(LocalDateTime.now());
                messageService.insertMessageSingleSender(messageFile);
                messageService.insertMessageSingleReceiver(messageFile);
                simpMessagingTemplate.convertAndSendToUser(messageFile.getReceiver().getId() + "", "/singleChat", messageFile);
                simpMessagingTemplate.convertAndSendToUser(messageFile.getSender().getId() + "", "/singleChat", messageFile);

                return messageFile;
            } else {
                messageText.setSenderDate(LocalDateTime.now());
                messageService.insertMessageSingleSender(messageText);
                messageService.insertMessageSingleReceiver(messageText);
                simpMessagingTemplate.convertAndSendToUser(messageText.getReceiver().getId() + "", "/singleChat", messageText);
                simpMessagingTemplate.convertAndSendToUser(messageText.getSender().getId() + "", "/singleChat", messageText);
                return messageText;
            }
        }
        return null;
    }

    @MessageMapping("/request-add-friend")
    public User requestAddFriend(@Payload String node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(node);
        String senderId = jsonNode.get("id").asText();
        String receiverId = jsonNode.get("receiverId").asText();

        FriendRequest result = userService.addRequestAddFriend(senderId, receiverId);
        if (result != null) {
            simpMessagingTemplate.convertAndSendToUser(receiverId, "/requestAddFriend", result);
            simpMessagingTemplate.convertAndSendToUser(senderId, "/requestAddFriend", result);
        }
        return null;
    }

    @MessageMapping("/grantRoleMember")
    public void grantRoleMember(@Payload ConversationGroup conversationGroup) {
        ConversationGroup groupRS = userService.grantRoleMember(conversationGroup);
        if (groupRS != null) {
            for (Member member : conversationGroup.getMembers()) {
                simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "/grantRoleMember", groupRS);
            }
        }
    }

    @MessageMapping("/accept-friend-request")
    public FriendRequest acceptFriendRequest(@Payload FriendRequest friendRequest) {
        System.out.println(friendRequest);
        if (userService.addFriend(friendRequest.getSender().getId(), friendRequest.getReceiver().getId())) {
            simpMessagingTemplate.convertAndSendToUser(friendRequest.getSender().getId(), "/acceptAddFriend", friendRequest);
            simpMessagingTemplate.convertAndSendToUser(friendRequest.getReceiver().getId(), "/acceptAddFriend", friendRequest);
            return friendRequest;
        }
        return null;
    }

    @MessageMapping("/unfriend")
    public Friend unFriend(@Payload String node) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(node);
        String ownerId = jsonNode.get("ownerId").asText();
        String userId = jsonNode.get("userId").asText();
        Friend friend = userService.Unfriend(ownerId, userId);
        System.out.println(friend);
        if (friend != null) {
            simpMessagingTemplate.convertAndSendToUser(ownerId, "/unfriend", friend);
            simpMessagingTemplate.convertAndSendToUser(userId, "/unfriend", friend);
            return friend;
        }
        return null;
    }

    @MessageMapping("/disbandConversation")
    public void disbandConversation(@Payload ConversationGroup conversationGroup) {
        System.out.println(conversationGroup);
        ConversationGroup group = userService.disbandConversation(conversationGroup);
        if (group != null) {
            for (Member member : conversationGroup.getMembers()) {
                simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "/disbandConversation", group);
            }
        }
    }


    @MessageMapping("/createGroup")
    public ConversationGroup createGroup(@Payload ConversationGroup conversationGroup) throws JsonProcessingException {
        System.out.println(conversationGroup);
        if (!conversationGroup.getMembers().isEmpty()) {
            Member userCreate = conversationGroup.getMembers().stream()
                    .filter(item -> item.getMemberType().equals(MemberType.GROUP_LEADER))
                    .findFirst()
                    .orElse(null);
            if (userCreate != null) {
                ConversationGroup groupRS = userService.createGroup(conversationGroup, userCreate);
                for (Member member : conversationGroup.getMembers()) {
                    simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "/createGroup", groupRS);
                }
                return groupRS;
            } else {
                simpMessagingTemplate.convertAndSendToUser(userCreate.getMember().getId(), "/createGroup", new Conversation());
            }
        }
        return null;
    }

    @MessageMapping("/decline-friend-request")
    public FriendRequest declineFriendRequest(@Payload FriendRequest friendRequest) {
        if (userService.removeFriendRequest(friendRequest.getSender().getId(), friendRequest.getReceiver().getId())) {
            simpMessagingTemplate.convertAndSendToUser(friendRequest.getSender().getId(), "/declineAddFriend", friendRequest);
            simpMessagingTemplate.convertAndSendToUser(friendRequest.getReceiver().getId(), "/declineAddFriend", friendRequest);
            return friendRequest;
        }
        return null;
    }


    @MessageMapping("QR")
    public SendQR recMessageQR(@Payload SendQR sendQR) {
        simpMessagingTemplate.convertAndSendToUser(sendQR.getIp(), "/QR", sendQR);
        System.out.println(sendQR);
        return sendQR;
    }

    @MessageMapping("/video")
    @SendTo("/video-call")
    public String sendVideo(String videoData) {
        // Xử lý dữ liệu video và gửi đến tất cả các subscriber
        System.out.println(videoData);
        simpMessagingTemplate.convertAndSendToUser("video", "/nhan", videoData);

        return videoData;
    }

    @MessageMapping("/removeMemberInGroup")
    public void removeMemberInGroup(@Payload String node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(node);
        String userId = rootNode.get("userId").asText();
        String idGroup = rootNode.get("idGroup").asText();
        System.out.println(node);
        System.out.println(userId);
        System.out.println(idGroup);
        ConversationGroup group = userService.removeMemberInGroup(userId, idGroup);
        if (group != null) {
            for (Member member : group.getMembers()) {
                simpMessagingTemplate.convertAndSendToUser(member.getMember().getId(), "/removeMemberInGroup", group);
            }
        }
    }
}
