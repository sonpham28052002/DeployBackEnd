package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.Conversation;
import vn.edu.iuh.fit.chat_backend.models.ConversationSingle;
import vn.edu.iuh.fit.chat_backend.models.Message;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                    userRepository.save(userSender.get());
                }
            }
        }
        message.setMessageType(MessageType.RETRIEVE);
        return message;
    }

    public boolean deleteMessage(Message message,String idGroup){
        System.out.println(message);
        System.out.println(idGroup.trim().equals(""));
        try {
            Optional<User> user = userRepository.findById(message.getSender().getId());
            List<Conversation> conversations = user.get().getConversation();
            if (idGroup.trim().equals("")){

            }else {
                for (Conversation conversation:conversations) {
                    if (conversation instanceof ConversationSingle){
                        if (((ConversationSingle) conversation).getUser().equals(User.builder().id(message.getReceiver().getId()).build())){
                            List<Message> messageList = conversation.getMessages();
                            messageList.remove(message);
                            conversation.setMessages(messageList);
                            conversation.setLastMessage();
                            userRepository.save(user.get());
                            break;
                        }
                    }
                }
            }
            return true;
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

}
