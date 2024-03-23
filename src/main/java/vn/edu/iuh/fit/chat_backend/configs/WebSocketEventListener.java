package vn.edu.iuh.fit.chat_backend.configs;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println(getUserId(event.getMessage().getHeaders()));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println(headers);
        System.out.println("Kết nối đã bị đóng: " );
    }

    public String getUserId(MessageHeaders payload) {
        GenericMessage<Object> message = (GenericMessage<Object>) payload.get("simpConnectMessage");
        String payloadHeader = null;
        if (message != null) {
            payloadHeader = message.getHeaders().get("nativeHeaders")+"";
            return "";
        }
        String a = "";
        if (payloadHeader != null) {
            a = payloadHeader.replaceAll("\\{", "");
            a = a.replaceAll("\\}", "");
            a = a.replaceAll("\\[", "");
            a = a.replaceAll("]", "");
        }
        String userId = a.split(",")[0];
        return userId.substring(userId.lastIndexOf("=") + 1);
    }
}
