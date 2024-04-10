package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    public boolean insertMessageSingleSender(Message message) {

        User sender = userRepository.findById(message.getSender().getId()).get();
        User receiver = message.getReceiver();
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

    public Message retrieveMessageSingle(Message message){
        System.out.println(message.getId());
        Optional<User> userReceiver = userRepository.findById(message.getReceiver().getId());
        Optional<User> userSender= userRepository.findById(message.getSender().getId());
        // update message list receiver
        List<Conversation> conversationsReceiver = userReceiver.get().getConversation();
        for (Conversation conversation:conversationsReceiver) {
            if (conversation instanceof ConversationSingle){
                if (((ConversationSingle) conversation).getUser().equals(userSender.get())){
                    List<Message> messageList = conversation.getMessages();
                    Message message1 =  messageList.get(messageList.indexOf(message));
                    message1.setMessageType(MessageType.RETRIEVE);
                    messageList.set(messageList.indexOf(message),message1);
                    conversation.setLastMessage();
                    conversation.setUpdateLast(LocalDateTime.now());
                    userRepository.save(userReceiver.get());
                }
            }
        }
        // update message list sender
        List<Conversation> conversationsSender = userSender.get().getConversation();
        for (Conversation conversation:conversationsSender) {
            if (conversation instanceof ConversationSingle){
                if (((ConversationSingle) conversation).getUser().equals(userReceiver.get())){
                    List<Message> messageList = conversation.getMessages();
                    Message message1 =  messageList.get(messageList.indexOf(message));
                    message1.setMessageType(MessageType.RETRIEVE);
                    messageList.set(messageList.indexOf(message),message1);
                    conversation.setLastMessage();
                    conversation.setUpdateLast(LocalDateTime.now());
                    userRepository.save(userSender.get());
                }
            }
        }
        message.setMessageType(MessageType.RETRIEVE);
        return message;
    }

    public boolean deleteMessageSingle(Message message, String ownerID){
        Optional<User> user = userRepository.findById(ownerID);
        List<Conversation> conversations = user.get().getConversation();
        try {
            if ("idGroup".trim().equals("")){
            }else {
                User userMember = null;
                if (user.get().getId().equals(message.getSender().getId())){
                    userMember = userRepository.findById(message.getReceiver().getId()).get();
                }else {
                    System.out.println(message.getSender().getId());
                    userMember = userRepository.findById(message.getSender().getId()).get();
                }
                for (Conversation conversation:conversations) {
                    if (conversation instanceof ConversationSingle){
                        if (((ConversationSingle) conversation).getUser().equals(userMember)){
                            List<Message> messageList = conversation.getMessages();
                            System.out.println(messageList.indexOf(message));
                            messageList.remove(messageList.indexOf(message));
                            conversation.setLastMessage();
                            userRepository.save(user.get());
                            return true;
                        }
                    }
                }
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

    public Conversation deleteConversation(String ownerId,String userID , String groupId){
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()){
            return null;
        }
        if (!groupId.trim().equals("")) {
            for (Conversation conversation:user.get().getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(groupId.trim())){
                    user.get().getConversation().remove(conversation);
                    System.out.println(conversation);
                    userRepository.save(user.get());
                    return conversation;
                }
            }
        }else {
            for (Conversation conversation:user.get().getConversation()) {
                if (conversation instanceof ConversationSingle && ((ConversationSingle) conversation).getUser().getId().trim().equals(userID)){
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
