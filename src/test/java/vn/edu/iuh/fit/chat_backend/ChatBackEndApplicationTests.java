package vn.edu.iuh.fit.chat_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.chat_backend.convert.AES;
import vn.edu.iuh.fit.chat_backend.convert.Encryption;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.AccountRepository;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import net.datafaker.Faker;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.services.UserService;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;
import vn.edu.iuh.fit.chat_backend.types.Gender;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class ChatBackEndApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void insertCon() {
        Faker faker = new Faker();

        // lấy user sonpham
        User sonpham = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        // lấy user cường
        User cuong = userRepository.findById("N7B7os8xFOMceSxRSIzQlkwr3N43").get();
        // lay user leon
        User leon = userRepository.findById("yGjQT5o0sleSmjHVDHT24SS8FAB2").get();
        // conversation sơn
        List<Conversation> conversationsSon = new ArrayList<>();
        // conversation cường
        List<Conversation> conversationsCuong = new ArrayList<>();
        // conversation leon
        List<Conversation> conversationsLeon = new ArrayList<>();

        // tạo conversation group
        List<Message> messageListGroup = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setSeen(List.of(User.builder().id(sonpham.getId()).build(), User.builder().id(cuong.getId()).build(), User.builder().id(leon.getId()).build()));
            if (i % 2 == 0) {
                messageText.setSender(User.builder().id(cuong.getId()).build());
            } else {
                messageText.setSender(User.builder().id(sonpham.getId()).build());
            }
            messageText.setSenderDate(LocalDateTime.now());
            messageListGroup.add(messageText);
        }
        ConversationGroup conversationGroup = new ConversationGroup();
        conversationGroup.setIdGroup(UUID.randomUUID().toString());
        conversationGroup.setAvtGroup(faker.internet().image());
        conversationGroup.setNameGroup(faker.company().name());
        conversationGroup.setConversationType(ConversationType.group);
        conversationGroup.setMessages(messageListGroup);
        conversationGroup.setMembers(List.of(User.builder().id(sonpham.getId()).build(), User.builder().id(cuong.getId()).build(), User.builder().id(leon.getId()).build()));
        conversationGroup.setUpdateLast(LocalDateTime.now());
        conversationGroup.setLastMessage();
        System.out.println(conversationGroup.getMessages());


        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setSeen(List.of(User.builder().id(sonpham.getId()).build(), User.builder().id(cuong.getId()).build()));
            if (i % 2 == 0) {
                messageText.setReceiver(User.builder().id(sonpham.getId()).build());
                messageText.setSender(User.builder().id(cuong.getId()).build());
            } else {
                messageText.setReceiver(User.builder().id(cuong.getId()).build());
                messageText.setSender(User.builder().id(sonpham.getId()).build());
            }
            messageText.setSenderDate(LocalDateTime.now());
            messageList.add(messageText);
        }
        // Tạo conversation single cường - sơn
        ConversationSingle conversationSingleCuong = new ConversationSingle();
        conversationSingleCuong.setMessages(messageList);
        conversationSingleCuong.setConversationType(ConversationType.single);
        conversationSingleCuong.setUser(User.builder().id(sonpham.getId()).build());
        conversationSingleCuong.setUpdateLast(LocalDateTime.now());
        conversationSingleCuong.setLastMessage();
        conversationsCuong.add(conversationSingleCuong);
        conversationsCuong.add(conversationGroup);
        cuong.setConversation(conversationsCuong);
        userRepository.save(cuong);

        // Tạo conversation single cường - sơn
        ConversationSingle conversationSingleSon = new ConversationSingle();
        conversationSingleSon.setMessages(messageList);
        conversationSingleSon.setConversationType(ConversationType.single);
        conversationSingleSon.setUser(User.builder().id(cuong.getId()).build());
        conversationSingleSon.setUpdateLast(LocalDateTime.now());
        conversationSingleSon.setLastMessage();
        conversationsSon.add(conversationSingleSon);

        for (int i = 0; i < 3; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setSeen(List.of(User.builder().id(sonpham.getId()).build(), User.builder().id(cuong.getId()).build()));
            if (i % 2 == 0) {
                messageText.setReceiver(User.builder().id(sonpham.getId()).build());
                messageText.setSender(User.builder().id(leon.getId()).build());
            } else {
                messageText.setReceiver(User.builder().id(leon.getId()).build());
                messageText.setSender(User.builder().id(sonpham.getId()).build());
            }
            messageText.setSenderDate(LocalDateTime.now());
            messageList.add(messageText);
        }
        ConversationSingle conversationSingleSonLeon = new ConversationSingle();
        conversationSingleSonLeon.setMessages(messageList);
        conversationSingleSonLeon.setConversationType(ConversationType.single);
        conversationSingleSonLeon.setUser(User.builder().id(leon.getId()).build());
        conversationSingleSonLeon.setUpdateLast(LocalDateTime.now());
        conversationSingleSonLeon.setLastMessage();
        conversationsLeon.add(conversationSingleSonLeon);
        conversationsLeon.add(conversationGroup);
        leon.setConversation(conversationsLeon);

        conversationsSon.add(conversationSingleSonLeon);
        conversationsSon.add(conversationGroup);
        sonpham.setConversation(conversationsSon);
        for (Conversation conversation : sonpham.getConversation()) {
            System.out.println(conversation);
        }
        userRepository.save(sonpham);

    }

    @Test
    void aa() {
        User leon = userRepository.findById("yGjQT5o0sleSmjHVDHT24SS8FAB2").get();
        User sonpham = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        List<Message> messageList = new ArrayList<>();
        for (Conversation conversation : sonpham.getConversation()) {
            if (conversation instanceof ConversationSingle) {
                if (((ConversationSingle) conversation).getUser().equals(User.builder().id(leon.getId()).build())) {
                    messageList.addAll(conversation.getMessages());
                }
            }
        }
        ConversationSingle conversationSingleLeon = new ConversationSingle();
        conversationSingleLeon.setMessages(messageList);
        conversationSingleLeon.setLastMessage();
        conversationSingleLeon.setConversationType(ConversationType.single);
        conversationSingleLeon.setUser(User.builder().id(sonpham.getId()).build());
        conversationSingleLeon.setUpdateLast(LocalDateTime.now());
        leon.setConversation(List.of(conversationSingleLeon));
        userRepository.save(leon);

    }

    @Test
    void test() {
        Faker faker = new Faker();
        User sonpham = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        User sonnguyen = userRepository.findById("RGpCgF0lR1aGVcttckhAbBHWcSp2").get();
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setSeen(List.of(User.builder().id(sonpham.getId()).build(), User.builder().id(sonnguyen.getId()).build()));
            if (i % 2 == 0) {
                messageText.setReceiver(User.builder().id(sonpham.getId()).build());
                messageText.setSender(User.builder().id(sonnguyen.getId()).build());
            } else {
                messageText.setReceiver(User.builder().id(sonnguyen.getId()).build());
                messageText.setSender(User.builder().id(sonpham.getId()).build());
            }
            messageText.setSenderDate(LocalDateTime.now());
            messageList.add(messageText);
        }
        // danh sách trò chuyện của sơn nguyễn
        List<Conversation> conversationsnguyen = new ArrayList<>();
        //  trò chuyện của sơn nguyễn - sonpham
        ConversationSingle conversationSingleSonLeon = new ConversationSingle();
        conversationSingleSonLeon.setMessages(messageList);
        conversationSingleSonLeon.setConversationType(ConversationType.single);
        conversationSingleSonLeon.setUser(User.builder().id(sonpham.getId()).build());
        conversationSingleSonLeon.setUpdateLast(LocalDateTime.now());
        conversationSingleSonLeon.setLastMessage();
        conversationsnguyen.add(conversationSingleSonLeon);
        sonnguyen.setConversation(conversationsnguyen);


        //  trò chuyện của sonpham - sơn nguyễn

        List<Conversation> conversationspham = sonpham.getConversation();
        ConversationSingle conversationSingleSon = new ConversationSingle();
        conversationSingleSon.setMessages(messageList);
        conversationSingleSon.setConversationType(ConversationType.single);
        conversationSingleSon.setUser(User.builder().id(sonnguyen.getId()).build());
        conversationSingleSon.setUpdateLast(LocalDateTime.now());
        conversationSingleSon.setLastMessage();
        conversationspham.add(conversationSingleSon);
        sonpham.setConversation(conversationspham);
        userRepository.save(sonnguyen);
        userRepository.save(sonpham);
    }

    @Test
    void kkk() {
        List<String> stringList = List.of("yGjQT5o0sleSmjHVDHT24SS8FAB2", "N7B7os8xFOMceSxRSIzQlkwr3N43", "RGpCgF0lR1aGVcttckhAbBHWcSp2", "Ukk2dSG2xlfYBOiih7C2pE7Ct542");
        User son = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        List<User> userList = userRepository.findAll();
        List<Friend> friends = new ArrayList<>();
        for (User user : userList) {
            System.out.println(user.getUserName() + " " + user.getId());
            if (stringList.contains(user.getId()) && !user.getId().equals(son.getId())) {
                friends.add(Friend.builder().user(User.builder().id(user.getId()).build()).build());
            }
        }
        son.setFriendList(friends);
        for (Friend friend : son.getFriendList()) {
            System.out.println(friend);
        }
        userRepository.save(son);
    }
}
