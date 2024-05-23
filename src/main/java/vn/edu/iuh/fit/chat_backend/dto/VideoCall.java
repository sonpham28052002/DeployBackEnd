package vn.edu.iuh.fit.chat_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import vn.edu.iuh.fit.chat_backend.models.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VideoCall {
    private String roomID;
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","avt","phone","userName","friendRequests"})
    private Set<User> users = new HashSet<>();
    private String idGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCall videoCall = (VideoCall) o;
        return Objects.equals(roomID, videoCall.roomID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID);
    }
}
