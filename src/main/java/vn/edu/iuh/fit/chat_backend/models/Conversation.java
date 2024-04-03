package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.ConversationType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Document("conversation")
@TypeAlias("conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private LocalDateTime updateLast;
    @JsonIgnore
    private List<Message> messages;
    private Message LastMessage ;
    private ConversationType conversationType;

    public void setLastMessage() {
        LastMessage = this.messages.get(this.messages.size()-1);;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "updateLast=" + updateLast +
                ", messages=" + messages +
                ", LastMessage=" + LastMessage +
                ", conversationType=" + conversationType +
                '}';
    }
}
