package vn.edu.iuh.fit.chat_backend.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Document("conversation")
@TypeAlias("conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Conversation {
    private LocalDateTime updateLast;
    private List<Message> messages;

    @Override
    public String toString() {
        return "Conversation{" +
                ", updateLast=" + updateLast +
                ", messages=" + messages +
                '}';
    }
}
