package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "messages")
@TypeAlias("messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    @Id
    @Indexed
    private String id;
    private MessageType messageType;
    private LocalDateTime senderDate;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","avt","phone","userName"})
    private User sender;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","avt","phone","userName"})
    private User receiver;
    private String react;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","avt","phone","userName"})
    private List<User> seen;
    private Message replyMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
