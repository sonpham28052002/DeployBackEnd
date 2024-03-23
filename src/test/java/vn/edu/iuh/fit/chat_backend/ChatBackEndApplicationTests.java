package vn.edu.iuh.fit.chat_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.AccountRepository;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import net.datafaker.Faker;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.services.UserService;
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
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;
    @Test
    void contextLoads() {
        System.out.println(userRepository.findAll());
        Faker faker = new Faker();
        User user = new User();
        user.setUserName(faker.twitter().userName());
        user.setId("1272");
        user.setGender(Gender.Nữ);
        user.setBio("hello");
        user.setAvt(faker.internet().image());
        user.setCoverImage(faker.internet().image());
        MessageFile message = new MessageFile();
        message.setId("1");
        message.setSize(10);
        message.setSender(userRepository.findById("1273").get());
//        user.setConversation(List.of(message));
        userRepository.save(user);
        System.out.println(LocalDateTime.now());
//		for (int i = 0; i < 5; i++) {
//			Account account = new Account();
//			account.setId("yGjQT5o0sleSmjHVDHT24SS8FAB"+i);
//			account.setPhone("+8498765432"+i);
//			account.setPassword("securepassword"+(i+1));
//			account.setCreateDate(LocalDateTime.now());
//			accountRepository.save(account);
//		}
//		System.out.println(accountRepository.findById("yGjQT5o0sleSmjHVDHT24SS8FAB0").get().getCreateDate());

    }

    @Test
    void insertAccount() {
        Account account = new Account();
        account.setId("jgfqCBTFdEgDmpHHXaNHdZV8B982");
        account.setPassword("sonpham28052002");
        account.setCreateDate(LocalDateTime.now());
        account.setPhone("84898168640");
        accountRepository.save(account);

        Account account1 = new Account();
        account1.setId("yGjQT5o0sleSmjHVDHT24SS8FAB2");
        account1.setPassword("leonpham170042002");
        account1.setCreateDate(LocalDateTime.now());
        account1.setPhone("84346676956");
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setId("N7B7os8xFOMceSxRSIzQlkwr3N43");
        account2.setPassword("cuongdacap123");
        account2.setCreateDate(LocalDateTime.now());
        account2.setPhone("84814929002");
        accountRepository.save(account2);

        Account account3 = new Account();
        account3.setId("Ukk2dSG2xlfYBOiih7C2pE7Ct542");
        account3.setPassword("bichnguyen123");
        account3.setCreateDate(LocalDateTime.now());
        account3.setPhone("84379046321");
        accountRepository.save(account3);
    }

    @Test
    void insertUser() {
        Faker faker = new Faker();

        User userSon = new User();
        userSon.setId("jgfqCBTFdEgDmpHHXaNHdZV8B982");
        userSon.setPhone("84898168640");
        userSon.setBio("hello");
        userSon.setAvt(faker.avatar().image());
        userSon.setCoverImage(faker.internet().image());
        userSon.setUserName("Phạm Thanh Sơn");
        userSon.setGender(Gender.Nam);
        userSon.setLogOut(LocalDateTime.now());
        userSon.setConversation(new ArrayList<>());
        userSon.setFriendList(new ArrayList<>());
        System.out.println(userSon);
        userRepository.save(userSon);

        User userLeon = new User();
        userLeon.setId("yGjQT5o0sleSmjHVDHT24SS8FAB2");
        userLeon.setPhone("84346676956");
        userLeon.setBio("hello");
        userLeon.setAvt(faker.avatar().image());
        userLeon.setCoverImage(faker.internet().image());
        userLeon.setUserName("Phạm Leon");
        userLeon.setGender(Gender.Nam);
        userLeon.setLogOut(LocalDateTime.now());
        userLeon.setConversation(new ArrayList<>());
        userLeon.setFriendList(new ArrayList<>());
        System.out.println(userLeon);
        userRepository.save(userLeon);

        User userCuong = new User();
        userCuong.setId("N7B7os8xFOMceSxRSIzQlkwr3N43");
        userCuong.setPhone("84814929002");
        userCuong.setBio("hello");
        userCuong.setAvt(faker.avatar().image());
        userCuong.setCoverImage(faker.internet().image());
        userCuong.setUserName("Nguyễn Chí Cường");
        userCuong.setGender(Gender.Nam);
        userCuong.setLogOut(LocalDateTime.now());
        userCuong.setConversation(new ArrayList<>());
        userCuong.setFriendList(new ArrayList<>());
        System.out.println(userCuong);
        userRepository.save(userCuong);

        User userBich = new User();
        userBich.setId("Ukk2dSG2xlfYBOiih7C2pE7Ct542");
        userBich.setPhone("84379046321");
        userBich.setBio("hello");
        userBich.setAvt(faker.avatar().image());
        userBich.setCoverImage(faker.internet().image());
        userBich.setUserName("Nguyễn Chí Cường");
        userBich.setGender(Gender.Nam);
        userBich.setLogOut(LocalDateTime.now());
        userBich.setConversation(new ArrayList<>());
        userBich.setFriendList(new ArrayList<>());
        System.out.println(userBich);
        userRepository.save(userBich);
    }

    @Test
    void insertConversation() {
        User userSon = userRepository.findById("jgfqCBTFdEgDmpHHXaNHdZV8B982").get();
        User userLeon = userRepository.findById("yGjQT5o0sleSmjHVDHT24SS8FAB2").get();

        // message Text
//        MessageText message = new MessageText();
//        message.setId(UUID.randomUUID().toString());
//        message.setSender(User.builder().id(userSon.getId()).avt(userSon.getAvt()).userName(userSon.getUserName()).build());
//        message.setReceiver(User.builder().id(userLeon.getId()).avt(userLeon.getAvt()).userName(userLeon.getUserName()).build());
//        message.setMessageType(MessageType.Text);
//        message.setSeen(List.of(User.builder().id(userSon.getId()).avt(userSon.getAvt()).userName(userSon.getUserName()).build()));
//        message.setSenderDate(LocalDateTime.now());
//        message.setContent("xin chào mày nha");
//        System.out.println(message);


        //message file
        MessageFile message = new MessageFile();
        message.setId(UUID.randomUUID().toString());
        message.setSender(User.builder().id(userSon.getId()).avt(userSon.getAvt()).userName(userSon.getUserName()).build());
        message.setReceiver(User.builder().id(userLeon.getId()).avt(userLeon.getAvt()).userName(userLeon.getUserName()).build());
        message.setMessageType(MessageType.JPG);
        message.setSeen(List.of(User.builder().id(userSon.getId()).avt(userSon.getAvt()).userName(userSon.getUserName()).build()));
        message.setSenderDate(LocalDateTime.now());
        message.setUrl("https://projectchatapp.s3.ap-southeast-1.amazonaws.com/134a357f-a817-47ea-90d1-3dc01f4117c0.jpg");
        message.setSize(46.2f);
        message.setTitleFile("mytam.jpg");
        System.out.println(message);
        messageService.insertMessageSingleSender(message);
        messageService.insertMessageSingleReceiver(message);

        //conversation
//        List<Conversation> conversationList = userSon.getConversation();
//        boolean containConversation = conversationList.contains(ConversationSingle.builder().user(User.builder().id("yGjQT5o0sleSmjHVDHT24SS8FAB2").build()).build());
//        if (!containConversation) {
//            System.out.println("not contain");
//            ConversationSingle conversationSingle = new ConversationSingle();
//            conversationSingle.setUser(User.builder().id(userLeon.getId()).avt(userLeon.getAvt()).userName(userLeon.getUserName()).build());
//            conversationSingle.setUpdateLast(LocalDateTime.now());
//            conversationSingle.setMessages(List.of(message));
//            conversationList.add(conversationSingle);
//            userSon.setConversation(conversationList);
//            userRepository.save(userSon);
//        } else {
//            System.out.println("contain");
//            int index = conversationList.indexOf(ConversationSingle.builder().user(User.builder().id("yGjQT5o0sleSmjHVDHT24SS8FAB2").build()).build());
//            Conversation conversation = conversationList.get(index);
//            List<Message> messageList =  conversation.getMessages();
//            messageList.add(message);
//            conversation.setMessages(messageList);
//            userRepository.save(userSon);
//        }


    }
}
