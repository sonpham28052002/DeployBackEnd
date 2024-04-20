package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Friend {
    @Id
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","phone","friendRequests"})
    private User user;
    private String tag;
    private String nickName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(user, friend.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
