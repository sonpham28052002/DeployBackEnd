package vn.edu.iuh.fit.chat_backend.models;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messagesText")
@TypeAlias("messagesText")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageText extends Message{
    private String content;

    @Override
    public String toString() {
        return "MessageText{" +
                "content='" + content + '\'' +
                '}';
    }
}
