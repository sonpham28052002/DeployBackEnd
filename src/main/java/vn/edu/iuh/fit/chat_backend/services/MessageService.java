package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;


    public boolean insertMessageSingleSender(Message message) {


        User sender = userRepository.findById(message.getSender().getId()).get();
        User receiver = message.getReceiver();
        message.setSenderDate(LocalDateTime.now());
        List<Conversation> conversationList = sender.getConversation();
        boolean containConversation = conversationList.contains(ConversationSingle.builder().user(User.builder().id(receiver.getId()).build()).build());
        if (!containConversation) {
            System.out.println("not contain");
            ConversationSingle conversationSingle = new ConversationSingle();
            conversationSingle.setUser(receiver);
            conversationSingle.setConversationType(ConversationType.single);
            conversationSingle.setUpdateLast(LocalDateTime.now());
            conversationSingle.setMessages(List.of(message));
            conversationSingle.setLastMessage();
            conversationSingle.setUpdateLast(LocalDateTime.now());
            conversationList.add(conversationSingle);
            sender.setConversation(conversationList);
            userRepository.save(sender);
        } else {
            System.out.println("contain");
            int index = conversationList.indexOf(ConversationSingle.builder().user(User.builder().id(receiver.getId()).build()).build());
            Conversation conversation = conversationList.get(index);
            conversation.setConversationType(ConversationType.single);
            conversationList.get(index).setUpdateLast(LocalDateTime.now());
            List<Message> messageList = conversation.getMessages();
            messageList.add(message);
            conversation.setMessages(messageList);
            conversation.setLastMessage();
            conversation.setUpdateLast(LocalDateTime.now());
            sender.setConversation(conversationList);
            userRepository.save(sender);
        }
        return true;
    }

    public List<Member> insertMessageGroup(Message message, String idGroup) {
        Optional<User> user = userRepository.findById(message.getSender().getId());
        if (user.isEmpty()) {
            return new ArrayList<>();
        }
        message.setSenderDate(LocalDateTime.now());
        List<Member> membersActive = new ArrayList<>();
        List<Conversation> conversationList = user.get().getConversation();
        for (Conversation conversation : conversationList) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                for (Member member : ((ConversationGroup) conversation).getMembers()) {
                    if (!member.getMemberType().equals(MemberType.LEFT_MEMBER)) {
                        addMessageGroupForMemberReceiver(message, idGroup, member.getMember().getId(), conversation);
                        System.out.println(member.getMember().getUserName());
                        membersActive.add(member);
                    }
                }
                return membersActive;
            }
        }
        return new ArrayList<>();
    }

    public boolean addMessageGroupForMemberReceiver(Message message, String idGroup, String idUserMember, Conversation conversationGroup) {
        try {
            Optional<User> user = userRepository.findById(idUserMember);
            List<Conversation> conversations = user.get().getConversation();
            for (Conversation conversation : conversations) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                    conversation.getMessages().add(message);
                    conversation.setLastMessage();
                    userRepository.save(user.get());
                    return true;
                }
            }
            conversationGroup.setMessages(List.of(message));
            conversationGroup.setLastMessage();
            conversationGroup.setConversationType(ConversationType.group);
            user.get().getConversation().add(conversationGroup);
            userRepository.save(user.get());
            return true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    public Message retrieveMessageSingle(Message message) {
        System.out.println(message.getId());
        Optional<User> userReceiver = userRepository.findById(message.getReceiver().getId());
        Optional<User> userSender = userRepository.findById(message.getSender().getId());
        // update message list receiver
        List<Conversation> conversationsReceiver = userReceiver.get().getConversation();
        for (Conversation conversation : conversationsReceiver) {
            if (conversation instanceof ConversationSingle) {
                if (((ConversationSingle) conversation).getUser().equals(userSender.get())) {
                    List<Message> messageList = conversation.getMessages();
                    Message message1 = messageList.get(messageList.indexOf(message));
                    message1.setMessageType(MessageType.RETRIEVE);
                    messageList.set(messageList.indexOf(message), message1);
                    conversation.setLastMessage();
                    conversation.setUpdateLast(LocalDateTime.now());
                    userRepository.save(userReceiver.get());
                }
            }
        }
        // update message list sender
        List<Conversation> conversationsSender = userSender.get().getConversation();
        for (Conversation conversation : conversationsSender) {
            if (conversation instanceof ConversationSingle) {
                if (((ConversationSingle) conversation).getUser().equals(userReceiver.get())) {
                    List<Message> messageList = conversation.getMessages();
                    Message message1 = messageList.get(messageList.indexOf(message));
                    message1.setMessageType(MessageType.RETRIEVE);
                    messageList.set(messageList.indexOf(message), message1);
                    conversation.setLastMessage();
                    conversation.setUpdateLast(LocalDateTime.now());
                    userRepository.save(userSender.get());
                }
            }
        }
        message.setMessageType(MessageType.RETRIEVE);
        return message;
    }


    public List<Member> retrieveMessageGroup(Message message, String idGroup) {
        System.out.println(message.getId());
        Optional<User> userSender = userRepository.findById(message.getSender().getId());
        List<Member> members = new ArrayList<>();
        List<Conversation> conversationsSender = userSender.get().getConversation();
        for (Conversation conversation : conversationsSender) {
            if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                for (Member member : ((ConversationGroup) conversation).getMembers()) {
                    if (updateMessageGroup(message, member.getMember().getId(), idGroup)) {
                        members.add(member);
                    }
                }
            }
        }
        message.setMessageType(MessageType.RETRIEVE);
        return members;
    }

    public boolean updateMessageGroup(Message message, String userID, String idGroup) {
        try {
            Optional<User> user = userRepository.findById(userID);
            for (Conversation conversation : user.get().getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                    List<Message> messageList = conversation.getMessages();
                    Message message1 = messageList.get(messageList.indexOf(message));
                    message1.setMessageType(MessageType.RETRIEVE);
                    messageList.set(messageList.indexOf(message), message1);
                    conversation.setLastMessage();
                    conversation.setUpdateLast(LocalDateTime.now());
                    userRepository.save(user.get());

                }
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessageGroup(Message message, String ownerID, String idGroup) {
        try (Stream<User> userDB = userRepository.findById(ownerID).stream()) {
            Optional<User> user = null;
            user = userDB.findFirst();
            for (Conversation conversation : user.get().getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())) {
                    List<Message> messageList = conversation.getMessages();
                    int index = messageList.indexOf(message);
                    messageList.remove(index);
                    if (index == 0) {
                        conversation.setLastMessage(null);
                    } else {
                        conversation.setLastMessage();
                    }
                    userRepository.save(user.get());
                    return true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessageSingle(Message message, String ownerID, String idGroup) {
        try (Stream<User> userDB = userRepository.findById(ownerID).stream()) {
            Optional<User> user = null;
            user = userDB.findFirst();

            User userCon = null;
            int indexCon = 0;
            if (user.get().getId().equals(message.getSender().getId())) {
                userCon = userRepository.findById(message.getReceiver().getId()).get();
            } else {
                userCon = userRepository.findById(message.getSender().getId()).get();
            }
            for (Conversation conversation : user.get().getConversation()) {
                if (conversation instanceof ConversationSingle && ((ConversationSingle) conversation).getUser().getId().equals(userCon.getId())) {
                    if (user.get().getConversation().get(indexCon).getMessages().size() <= 3) {
                        int indexMess = user.get().getConversation().get(indexCon).getMessages().indexOf(message);
                        List<Message> list = new ArrayList<>();
                        for (int i = 0; i < user.get().getConversation().get(indexCon).getMessages().size(); i++) {
                            if (i != indexMess) {
                                list.add(user.get().getConversation().get(indexCon).getMessages().get(i));
                            }
                        }
                        conversation.setMessages(list);
                        user.get().getConversation().set(indexCon, conversation);
                        userRepository.save(user.get());
                    } else {
                        conversation.getMessages().remove(message);
                        if (conversation.getMessages().size() == 0){
                            conversation.setLastMessage(null);
                        }else {
                            conversation.setLastMessage();
                        }
                        System.out.println(conversation.getMessages().size());
                        System.out.println(user.get().getConversation().get(indexCon).getMessages().size());
                        userRepository.save(user.get());
                    }
                    return true;
                }
                indexCon++;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean insertMessageSingleReceiver(Message message) {

        User sender = message.getSender();
        User receiver = userRepository.findById(message.getReceiver().getId()).get();
        List<Conversation> conversationList = receiver.getConversation();
        boolean containConversation = conversationList.contains(ConversationSingle.builder().user(User.builder().id(sender.getId()).build()).build());
        if (!containConversation) {
            System.out.println("not contain");
            ConversationSingle conversationSingle = new ConversationSingle();
            conversationSingle.setUser(sender);
            conversationSingle.setUpdateLast(LocalDateTime.now());
            conversationSingle.setMessages(List.of(message));
            conversationSingle.setLastMessage();
            conversationList.add(conversationSingle);
            receiver.setConversation(conversationList);
            userRepository.save(receiver);
        } else {
            System.out.println("contain");
            int index = conversationList.indexOf(ConversationSingle.builder().user(User.builder().id(sender.getId()).build()).build());
            Conversation conversation = conversationList.get(index);
            conversation.setUpdateLast(LocalDateTime.now());
            List<Message> messageList = conversation.getMessages();
            messageList.add(message);
            conversation.setMessages(messageList);
            conversation.setLastMessage();
            receiver.setConversation(conversationList);
            userRepository.save(receiver);
        }
        return true;
    }

    public Conversation deleteConversation(String ownerId, String userID, String groupId) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            return null;
        }
        if (!groupId.trim().equals("")) {
            for (Conversation conversation : user.get().getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(groupId.trim())) {
                    user.get().getConversation().remove(conversation);
                    System.out.println(conversation);
                    userRepository.save(user.get());
                    return conversation;
                }
            }
        } else {
            for (Conversation conversation : user.get().getConversation()) {
                if (conversation instanceof ConversationSingle && ((ConversationSingle) conversation).getUser().getId().trim().equals(userID)) {
                    user.get().getConversation().remove(conversation);
                    System.out.println(conversation);
                    userRepository.save(user.get());

                    return conversation;
                }
            }
        }
        return null;
    }
}
