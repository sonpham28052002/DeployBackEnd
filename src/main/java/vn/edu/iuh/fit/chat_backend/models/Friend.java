package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Friend {
    private String nickName;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut"})
    private User user;
    private String tag;

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
