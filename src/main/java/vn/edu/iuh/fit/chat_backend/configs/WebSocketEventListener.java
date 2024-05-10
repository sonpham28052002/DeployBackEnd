package vn.edu.iuh.fit.chat_backend.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vn.edu.iuh.fit.chat_backend.models.Friend;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class WebSocketEventListener {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;

    private HashMap<String,String> userConnect = new HashMap<String, String>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        if (getUserId(event.getMessage().getHeaders()) != null){
            String idSession =  StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
            String idUser = getUserId(event.getMessage().getHeaders());
            userConnect.put(idSession,idUser);
            sendUserOnline(idUser);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String idSession = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        String idUser = userConnect.get(idSession);
        userConnect.remove(idSession);
        if(idUser != null){
            sendUserOffline(idUser);
        }
    }

    public String getUserId(MessageHeaders payload) {
        GenericMessage<Object> message = (GenericMessage<Object>) payload.get("simpConnectMessage");
        String payloadHeader = null;
        if (message != null) {
            payloadHeader = message.getHeaders().get("nativeHeaders")+"";
        }
        String a = "";
        if (payloadHeader != null) {
            a = payloadHeader.replaceAll("\\{", "");
            a = a.replaceAll("\\}", "");
            a = a.replaceAll("\\[", "");
            a = a.replaceAll("]", "");
        }
        String userId = a.split(",")[0];
        if (userId.substring(0,userId.lastIndexOf("=")).equals("login") || userId.substring(0,userId.lastIndexOf("=")).equals("0")){
            return userId.substring(userId.lastIndexOf("=") + 1);
        }
       return null;
    }

    public void sendUserOnline(String id){
        User user = userRepository.findById(id).get();
        List<String> idUserOnline = new ArrayList<>();
        for (Friend friend:user.getFriendList()) {
            if (userConnect.containsValue(friend.getUser().getId())){
                idUserOnline.add(friend.getUser().getId());
                simpMessagingTemplate.convertAndSendToUser(friend.getUser().getId(), "userOnline", id);
            }
        }
        simpMessagingTemplate.convertAndSendToUser(id, "ListUserOnline", idUserOnline);
    }
    public void sendUserOffline(String id){
        User user = userRepository.findById(id).get();
        List<String> idUserOnline = new ArrayList<>();
        for (Friend friend:user.getFriendList()) {
            if (userConnect.containsValue(friend.getUser().getId())){
                idUserOnline.add(friend.getUser().getId());
                simpMessagingTemplate.convertAndSendToUser(friend.getUser().getId(), "userOffline", id);
            }
        }
    }
}
