package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.Friend;
import vn.edu.iuh.fit.chat_backend.models.FriendRequest;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Kết bạn giữa 2 user
     * @param userIdA id của người gửi lời mời
     * @param userIdB id của người đồng ý kết bạn
     * @return kết quả thực hiện
     */
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

            // xoá lời mời kết bạn của A
            List<FriendRequest> friendRequestListA = userA.getFriendRequests();
            friendRequestListA.removeIf(item -> item.getSender().equals(userA) && item.getReceiver().equals(userB));
            userA.setFriendRequests(friendRequestListA);

            // xoá lời mời kết bạn của A
            List<FriendRequest> friendRequestListB = userB.getFriendRequests();
            friendRequestListB.removeIf(item -> item.getSender().equals(userA) && item.getReceiver().equals(userB));
            userB.setFriendRequests(friendRequestListB);

            userRepository.save(userB);
            userRepository.save(userA);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Xoá lời mời kết bạn
     * @param senderId id của người gửi lời mời
     * @param receiverId id của người nhận được lời mời
     * @return kết quả xử lý
     */
    public boolean removeFriendRequest(String senderId, String receiverId){
        try{
            User sender = userRepository.findById(senderId).get();
            User receiver = userRepository.findById(receiverId).get();

            List<FriendRequest> friendRequestListSender = sender.getFriendRequests();
            friendRequestListSender.removeIf(item -> item.getSender().equals(sender) && item.getReceiver().equals(receiver));
            sender.setFriendRequests(friendRequestListSender);

            List<FriendRequest> friendRequestListReceiver = receiver.getFriendRequests();
            friendRequestListReceiver.removeIf(item -> item.getSender().equals(sender) && item.getReceiver().equals(receiver));
            receiver.setFriendRequests(friendRequestListReceiver);

            userRepository.save(sender);
            userRepository.save(receiver);
            return true;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm một yêu cầu kết bạn từ user có id senderId đến user có id receiverId
     * @param senderId id của người yêu cầu kết bạn
     * @param receiverId id của người nhận được yêu cầu
     * @return kết quả thêm yêu cầu
     */
    public boolean addRequestAddFriend(String senderId, String receiverId){
        try {
            User sender = userRepository.findById(senderId).get();
            User receiver = userRepository.findById(receiverId).get();

            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setSender(User.builder().id(sender.getId()).userName(sender.getUserName()).avt(sender.getAvt()).build());
            friendRequest.setReceiver(User.builder().id(receiver.getId()).userName(receiver.getUserName()).avt(receiver.getAvt()).build());
            friendRequest.setSendDate(LocalDateTime.now());

//            FriendRequest friendRequestReceiver = new FriendRequest();
//            friendRequestReceiver.setSender(User.builder().id(sender.getId()).userName(sender.getUserName()).avt(sender.getAvt()).build());
//            friendRequestReceiver.setReceiver(User.builder().id(receiver.getId()).userName(receiver.getUserName()).avt(receiver.getAvt()).build());
//            friendRequestReceiver.setSendDate(LocalDateTime.now());

            List<FriendRequest> friendRequestListSender = sender.getFriendRequests();
            friendRequestListSender.add(friendRequest);
            sender.setFriendRequests(friendRequestListSender);

            List<FriendRequest> friendRequestListReceiver = receiver.getFriendRequests();
            friendRequestListReceiver.add(friendRequest);
            receiver.setFriendRequests(friendRequestListReceiver);

            userRepository.save(sender);
            userRepository.save(receiver);
            return true;
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return false;
    }
}
