package vn.edu.iuh.fit.chat_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.MemberType;

import java.util.Objects;

@Document("Member")
@TypeAlias("Member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
    @JsonIgnoreProperties(value = {"conversation", "friendList", "gender", "coverImage", "bio", "phone", "logOut", "dob", "phone",})
    private User member;
    private MemberType memberType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member1 = (Member) o;
        return Objects.equals(member, member1.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member);
    }
}
