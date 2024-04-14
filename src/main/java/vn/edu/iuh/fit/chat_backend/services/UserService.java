package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;
import vn.edu.iuh.fit.chat_backend.types.GroupStatus;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Kết bạn giữa 2 user
     *
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
            friendA.setUser(User.builder().id(userB.getId()).build());
            friendA.setNickName(userB.getUserName());
            friendA.setTag("");

            //add user A
            Friend friendB = new Friend();
            friendB.setUser(User.builder().id(userA.getId()).build());
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

    public Friend Unfriend(String ownerId, String userId) {
        Optional<User> user = userRepository.findById(ownerId);
        List<Friend> friendList = user.get().getFriendList();
        Friend friend = Friend.builder().user(User.builder().id(userId).build()).build();
        int index = friendList.indexOf(friend);
        Friend friendRS = null;
        if (index != -1) {
            friendRS = friendList.get(index);
            friendList.remove(index);
            user.get().setFriendList(friendList);
            userRepository.save(user.get());
        }
        return friendRS;
    }

    public ConversationGroup disbandConversation(ConversationGroup conversationGroup){
        try {
            for (Member member:conversationGroup.getMembers()) {
                User user =userRepository.findById(member.getMember().getId()).get();
                for (Conversation conversation:user.getConversation()) {
                    if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(conversationGroup.getIdGroup().trim())){
                        ((ConversationGroup) conversation).setStatus(GroupStatus.DISBANDED);
                        break;
                    }
                }
                userRepository.save(user);
            }
            conversationGroup.setStatus(GroupStatus.DISBANDED);
            return conversationGroup;
        }catch (Exception  exception){
            exception.printStackTrace();
        }
        return null;
    }

    public Optional<User> getUserByPhone(String phone) {
        Optional<User> user = userRepository.getUserByPhone(phone);
        user.get().setConversation(new ArrayList<>());
        user.get().setFriendList(new ArrayList<>());
        return user;
    }

    /**
     * Xoá lời mời kết bạn
     *
     * @param senderId   id của người gửi lời mời
     * @param receiverId id của người nhận được lời mời
     * @return kết quả xử lý
     */
    public boolean removeFriendRequest(String senderId, String receiverId) {
        try {
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm một yêu cầu kết bạn từ user có id senderId đến user có id receiverId
     *
     * @param senderId   id của người yêu cầu kết bạn
     * @param receiverId id của người nhận được yêu cầu
     * @return kết quả thêm yêu cầu
     */
    public FriendRequest addRequestAddFriend(String senderId, String receiverId) {
        try {
            Optional<User> sender = userRepository.findById(senderId);
            Optional<User> receiver = userRepository.findById(receiverId);
            System.out.println(senderId);

            if (receiver.isEmpty() || sender.isEmpty()) {
                return null;
            }

            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setSender(User.builder().id(sender.get().getId()).build());
            friendRequest.setReceiver(User.builder().id(receiver.get().getId()).build());
            friendRequest.setSendDate(LocalDateTime.now());

            List<FriendRequest> friendRequestListSender = sender.get().getFriendRequests();
            friendRequestListSender.add(friendRequest);
            sender.get().setFriendRequests(friendRequestListSender);

            List<FriendRequest> friendRequestListReceiver = receiver.get().getFriendRequests();
            friendRequestListReceiver.add(friendRequest);
            receiver.get().setFriendRequests(friendRequestListReceiver);

            userRepository.save(sender.get());
            userRepository.save(receiver.get());

            friendRequest.setSender(User.builder().id(sender.get().getId()).userName(sender.get().getUserName()).avt(sender.get().getAvt()).build());
            friendRequest.setReceiver(User.builder().id(receiver.get().getId()).userName(receiver.get().getUserName()).avt(receiver.get().getAvt()).build());
            return friendRequest;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ConversationGroup createGroup(ConversationGroup conversationGroup,Member member ) {
        User userCreate = userRepository.findById(member.getMember().getId()).get();
        ConversationGroup newConversation = new ConversationGroup();
        try {
            if (conversationGroup.getStatus() == null) {
                newConversation.setStatus(GroupStatus.ACTIVE);
            }else{
                newConversation.setStatus(conversationGroup.getStatus());
            }
            if (conversationGroup.getAvtGroup() == null){
                newConversation.setAvtGroup("https://inkythuatso.com/uploads/images/2023/03/anh-dai-dien-trang-inkythuatso-03-15-23-52.jpg");
            }else{
                newConversation.setAvtGroup(conversationGroup.getAvtGroup());
            }
            newConversation.setIdGroup(UUID.randomUUID().toString());
            newConversation.setNameGroup(conversationGroup.getNameGroup());
            newConversation.setConversationType(ConversationType.group);
            newConversation.setMessages(new ArrayList<>());
            List<Member> members = new ArrayList<>();
            for (Member member1:conversationGroup.getMembers()) {
                members.add( Member.builder()
                        .member(User.builder().id(member1.getMember().getId()).build())
                        .memberType(member1.getMemberType()).build());
            }
            newConversation.setMembers(members);

            newConversation.setUpdateLast(LocalDateTime.now());

            userCreate.getConversation().add(newConversation);
            for (Member member1:newConversation.getMembers()) {
                User user = userRepository.findById(member1.getMember().getId()).get();
                user.getConversation().add(newConversation);
                userRepository.save(user);
            }
            return newConversation;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ConversationGroup();
    }


    public ConversationGroup grantRoleMember(ConversationGroup conversationGroup){
        try{
            for (Member member:conversationGroup.getMembers()) {
                User user = userRepository.findById(member.getMember().getId()).get();
                List<Conversation> conversations = user.getConversation();
                int index = 0;
                for (Conversation conversation:conversations) {
                    if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(conversationGroup.getIdGroup())){
                        ((ConversationGroup) conversation).setMembers(conversationGroup.getMembers());
                        conversations.set(index, conversation);
                        user.setConversation(conversations);
                        userRepository.save(user);
                        break;
                    }
                    index++;
                }
            }
            return conversationGroup;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param conversationGroup chứa idGroup và danh sách các member cần phân quyền phó nhóm
     * @param ownerId người phân quyền
     * @return conversationGroup đã cập nhật
     */
    public ConversationGroup grantRoleMemberV2(ConversationGroup conversationGroup, String ownerId){
        try{
            List<Member> membersDEPUTYLEADER = conversationGroup.getMembers();
            System.out.println(ownerId);
            User owner = userRepository.findById(ownerId).get();
            ConversationGroup group = null;
            for (Conversation conversation:owner.getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(conversationGroup.getIdGroup().trim())){
                    for (Member member:((ConversationGroup) conversation).getMembers()) {
                        if (membersDEPUTYLEADER.contains(member)){
                            member.setMemberType(MemberType.DEPUTY_LEADER);
                        }else if(!member.getMember().getId().trim().equals(ownerId.trim()) && !member.getMemberType().equals(MemberType.LEFT_MEMBER)){
                            member.setMemberType(MemberType.MEMBER);
                        }
                    }
                    group = (ConversationGroup) conversation;
                    break;
                }
            }
            if (group !=null){
                for (Member member:group.getMembers()) {
                    User user = userRepository.findById(member.getMember().getId()).get();
                    for (Conversation conversation:user.getConversation()) {
                        if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(group.getIdGroup().trim())){
                            ((ConversationGroup) conversation).setMembers(group.getMembers());
                            userRepository.save(user);
                            break;
                        }
                    }
                }
                return group;
            }
            return null;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param group chứa idGroup và danh sách các member mới được thêm
     * @param ownerId người thêm
     * @return conversationGroup đã cập nhật
     */
    public ConversationGroup addMemberNew(ConversationGroup group, String ownerId){


        return null;
    }

    public ConversationGroup removeMemberInGroup(String userId, String idGroup){
        try {
            int index = 0;
            ConversationGroup group = null;
            User user = userRepository.findById(userId).get();
            for (Conversation conversation:user.getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(idGroup.trim())){
                    User user1 = userRepository.findById(userId).get();
                    Member memberRemove = Member.builder().member(User.builder().id(userId.trim()).build()).memberType(MemberType.LEFT_MEMBER).build();
                    index = ((ConversationGroup) conversation).getMembers().indexOf(memberRemove);
                    ((ConversationGroup) conversation).getMembers().set(index, memberRemove);
                    group = (ConversationGroup) conversation;
                }
            }
            if (group != null){
                for (Member member:group.getMembers()) {
                    int i = 0;
                    User user1 = userRepository.findById(member.getMember().getId()).get();
                    for (Conversation conversation:user1.getConversation()) {
                        if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(group.getIdGroup().trim())){
                            user1.getConversation().set(i,group);
                            userRepository.save(user1);
                            break;
                        }
                        i++;
                    }
                }
                return group;
            }
            return null;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public ConversationGroup changeStatusGroup(ConversationGroup group, String ownerId){
        try {
            User owner = userRepository.findById(ownerId).get();
            ConversationGroup conversationGroup = null;
            for (Conversation conversation:owner.getConversation()) {
                if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(group.getIdGroup().trim())){
                    conversationGroup = (ConversationGroup) conversation;
                    break;
                }
            }
            if (conversationGroup != null){
                conversationGroup.setStatus(group.getStatus());
                for (Member member:conversationGroup.getMembers()) {
                    User user = userRepository.findById(member.getMember().getId()).get();
                    for (Conversation conversation:user.getConversation()) {
                        if (conversation instanceof ConversationGroup && ((ConversationGroup) conversation).getIdGroup().trim().equals(group.getIdGroup().trim())){
                            ((ConversationGroup) conversation).setStatus(conversationGroup.getStatus());
//                            userRepository.save(user);
                            break;
                        }
                    }
                }
                return conversationGroup;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }
}
