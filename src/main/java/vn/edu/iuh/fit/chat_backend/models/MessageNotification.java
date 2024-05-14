package vn.edu.iuh.fit.chat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.NotificationType;

@Document(collection = "messageNotification")
@TypeAlias("messageNotification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageNotification extends Message{
    private NotificationType notificationType;
    private User user;
    private String content;

}
