package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document("friendRequests")
@TypeAlias("friendRequests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FriendRequest {
    private LocalDateTime sendDate;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","phone"})
    private User sender;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","phone"})
    private User receiver;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequest that)) return false;
        return Objects.equals(getSendDate(), that.getSendDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSendDate());
    }
}
