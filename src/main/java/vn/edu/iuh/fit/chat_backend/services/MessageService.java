package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.Conversation;
import vn.edu.iuh.fit.chat_backend.models.ConversationSingle;
import vn.edu.iuh.fit.chat_backend.models.Message;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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
