package vn.edu.iuh.fit.chat_backend.DateLoader;

import com.azure.core.annotation.Get;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;
import vn.edu.iuh.fit.chat_backend.types.GroupStatus;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/DataLoader")
public class DataLoader {
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public void loadData(){
        insertCon();
        test();
        test2();
        test3();
        test4();
        kkk();
        aahi();
    }

    void insertCon() {
        Faker faker = new Faker();

        // lấy user sonpham
        User sonpham = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        // lấy user cường
        User cuong = userRepository.findById("N7B7os8xFOMceSxRSIzQlkwr3N43").get();
        // lay user leon
        User leon = userRepository.findById("yGjQT5o0sleSmjHVDHT24SS8FAB2").get();
        User sonnguyen = userRepository.findById("RGpCgF0lR1aGVcttckhAbBHWcSp2").get();

        // conversation sơn
        List<Conversation> conversationsSon = new ArrayList<>();
        // conversation cường
        List<Conversation> conversationsCuong = new ArrayList<>();
        // conversation leon
        List<Conversation> conversationsLeon = new ArrayList<>();

        // tạo conversation group
        List<Message> messageListGroup = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setReact(new ArrayList<>());
            messageText.setSeen(Set.of(User.builder().id(sonpham.getId()).build(), User.builder().id(cuong.getId()).build(), User.builder().id(leon.getId()).build()));
            if (i % 2 == 0) {
                messageText.setSender(User.builder().id(cuong.getId()).build());
            } else if (i % 3 == 0) {
                messageText.setSender(User.builder().id(leon.getId()).build());
            } else if (i % 5 == 0) {
                messageText.setSender(User.builder().id(sonnguyen.getId()).build());
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
        conversationGroup.setStatus(GroupStatus.ACTIVE);
        conversationGroup.setMessages(messageListGroup);
        conversationGroup.setMembers(List.of(
                Member.builder()
                        .member(User.builder().id(sonpham.getId()).build())
                        .memberType(MemberType.GROUP_LEADER).build(),
                Member.builder()
                        .member(User.builder().id(cuong.getId()).build())
                        .memberType(MemberType.DEPUTY_LEADER).build(),
                Member.builder()
                        .member(User.builder().id(leon.getId()).build())
                        .memberType(MemberType.MEMBER).build(),
                Member.builder()
                        .member(User.builder().id(sonnguyen.getId()).build())
                        .memberType(MemberType.DEPUTY_LEADER).build()
        ));

        conversationGroup.setUpdateLast(LocalDateTime.now());
        conversationGroup.setLastMessage();
        System.out.println(conversationGroup.getMessages());


        sonnguyen.setConversation(List.of(conversationGroup));
        sonpham.setConversation(List.of(conversationGroup));
        cuong.setConversation(List.of(conversationGroup));
        leon.setConversation(List.of(conversationGroup));

        userRepository.save(sonnguyen);
        userRepository.save(cuong);
        userRepository.save(sonpham);
        userRepository.save(leon);

    }


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
            messageText.setReact(new ArrayList<>());
            messageText.setSeen(Set.of(User.builder().id(sonpham.getId()).build(), User.builder().id(sonnguyen.getId()).build()));
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
        List<Conversation> conversationsnguyen = sonnguyen.getConversation();
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


    void test2() {
        Faker faker = new Faker();
        User sonpham = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        User leon = userRepository.findById("yGjQT5o0sleSmjHVDHT24SS8FAB2").get();
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setReact(new ArrayList<>());
            messageText.setSeen(Set.of(User.builder().id(sonpham.getId()).build(), User.builder().id(leon.getId()).build()));
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
        // danh sách trò chuyện của sơn nguyễn
        List<Conversation> conversationsnguyen = leon.getConversation();
        //  trò chuyện của sơn nguyễn - sonpham
        ConversationSingle conversationSingleSonLeon = new ConversationSingle();
        conversationSingleSonLeon.setMessages(messageList);
        conversationSingleSonLeon.setConversationType(ConversationType.single);
        conversationSingleSonLeon.setUser(User.builder().id(sonpham.getId()).build());
        conversationSingleSonLeon.setUpdateLast(LocalDateTime.now());
        conversationSingleSonLeon.setLastMessage();
        conversationsnguyen.add(conversationSingleSonLeon);
        leon.setConversation(conversationsnguyen);


        //  trò chuyện của sonpham - sơn nguyễn

        List<Conversation> conversationspham = sonpham.getConversation();
        ConversationSingle conversationSingleSon = new ConversationSingle();
        conversationSingleSon.setMessages(messageList);
        conversationSingleSon.setConversationType(ConversationType.single);
        conversationSingleSon.setUser(User.builder().id(leon.getId()).build());
        conversationSingleSon.setUpdateLast(LocalDateTime.now());
        conversationSingleSon.setLastMessage();
        conversationspham.add(conversationSingleSon);
        sonpham.setConversation(conversationspham);
        userRepository.save(leon);
        userRepository.save(sonpham);
    }

    void test3() {
        Faker faker = new Faker();
        User sonpham = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        User cuong = userRepository.findById("N7B7os8xFOMceSxRSIzQlkwr3N43").get();
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageText messageText = new MessageText();
            messageText.setMessageType(MessageType.Text);
            messageText.setContent(faker.text().text());
            messageText.setId(UUID.randomUUID().toString());
            messageText.setReact(new ArrayList<>());
            messageText.setSeen(Set.of(User.builder().id(sonpham.getId()).build(), User.builder().id(cuong.getId()).build()));
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
        // danh sách trò chuyện của sơn nguyễn
        List<Conversation> conversationsnguyen = cuong.getConversation();
        //  trò chuyện của sơn nguyễn - sonpham
        ConversationSingle conversationSingleSonLeon = new ConversationSingle();
        conversationSingleSonLeon.setMessages(messageList);
        conversationSingleSonLeon.setConversationType(ConversationType.single);
        conversationSingleSonLeon.setUser(User.builder().id(sonpham.getId()).build());
        conversationSingleSonLeon.setUpdateLast(LocalDateTime.now());
        conversationSingleSonLeon.setLastMessage();
        conversationsnguyen.add(conversationSingleSonLeon);
        cuong.setConversation(conversationsnguyen);


        //  trò chuyện của sonpham - sơn nguyễn

        List<Conversation> conversationspham = sonpham.getConversation();
        ConversationSingle conversationSingleSon = new ConversationSingle();
        conversationSingleSon.setMessages(messageList);
        conversationSingleSon.setConversationType(ConversationType.single);
        conversationSingleSon.setUser(User.builder().id(cuong.getId()).build());
        conversationSingleSon.setUpdateLast(LocalDateTime.now());
        conversationSingleSon.setLastMessage();
        conversationspham.add(conversationSingleSon);
        sonpham.setConversation(conversationspham);
        userRepository.save(cuong);
        userRepository.save(sonpham);
    }
    void test4() {
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
    void kkk() {
        List<String> stringList = List.of("yGjQT5o0sleSmjHVDHT24SS8FAB2", "N7B7os8xFOMceSxRSIzQlkwr3N43", "RGpCgF0lR1aGVcttckhAbBHWcSp2");
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

    void aahi() {
        List<User> userList = userRepository.findAll();
        for (User user:userList) {
            user.setFriendRequests(new ArrayList<>());
            userRepository.save(user);
        }
    }
}
