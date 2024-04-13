package vn.edu.iuh.fit.chat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.types.GroupStatus;

import java.util.List;
import java.util.Objects;

@Document("ConversationGroup")
@TypeAlias("ConversationGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationGroup extends Conversation {
    private String idGroup;
    private List<Member> members;
    private String avtGroup;
    private String nameGroup;
    private GroupStatus status;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationGroup that = (ConversationGroup) o;
        return Objects.equals(idGroup, that.idGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGroup);
    }

    @Override
    public String toString() {
        return "ConversationGroup{" +
                "idGroup='" + idGroup + '\'' +
                ", members=" + members +
                '}';
    }
}
