package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.Emotion;

import java.util.Objects;

@Document(collection = "React")
@TypeAlias("React")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class React {
    @JsonIgnoreProperties(value = {"conversation", "friendList","gender","coverImage","bio","phone","logOut","dob","avt","phone","userName","friendRequests"})
    private User user;
    private Emotion react;
}
