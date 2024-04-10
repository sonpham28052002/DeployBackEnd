package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.Friend;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean addFriend(String userIdA, String userIdB) {
        try {
            User userA = userRepository.findById(userIdA).get();
            User userB = userRepository.findById(userIdB).get();

            //add user B
            Friend friendA = new Friend();
            friendA.setUser(User.builder().id(userB.getId()).avt(userB.getAvt()).userName(userB.getUserName()).build());
            friendA.setNickName(userB.getUserName());
            friendA.setTag("");

            //add user A
            Friend friendB = new Friend();
            friendB.setUser(User.builder().id(userA.getId()).avt(userA.getAvt()).userName(userA.getUserName()).build());
            friendB.setNickName(userA.getUserName());
            friendB.setTag("");

            // danh sach friend cua A
            List<Friend> friendsA = userA.getFriendList();
            friendsA.add(friendA);
            userA.setFriendList(friendsA);

            // danh sach friend của B
            List<Friend> friendsB = userB.getFriendList();
            friendsB.add(friendB);
            userB.setFriendList(friendsB);

            userRepository.save(userB);
            userRepository.save(userA);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
    public Optional<User> getUserByPhone(String phone){
        Optional<User> user = userRepository.getUserByPhone(phone);
        user.get().setConversation(new ArrayList<>());
        user.get().setFriendList(new ArrayList<>());
        return user;
    }
}
