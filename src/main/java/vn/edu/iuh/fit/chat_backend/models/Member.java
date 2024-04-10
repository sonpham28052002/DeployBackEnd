package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.MemberType;
@Document("Member")
@TypeAlias("Member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @JsonIgnoreProperties(value = {"conversation", "friendList", "gender", "coverImage", "bio", "phone", "logOut", "dob", "phone",})
    private User member;
    private MemberType memberType;
}
