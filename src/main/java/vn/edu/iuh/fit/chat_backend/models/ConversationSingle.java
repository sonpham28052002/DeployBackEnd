package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("ConversationSingle")
@TypeAlias("ConversationSingle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationSingle extends Conversation{
    @JsonIgnoreProperties(value = {"conversation", "friends","gender","coverImage","bio"})
    private User user;
    @Override
    public String toString() {
        return "ConversationSingle{" +
                "user=" + user +
                "date=" + super.getUpdateLast() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationSingle that = (ConversationSingle) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
