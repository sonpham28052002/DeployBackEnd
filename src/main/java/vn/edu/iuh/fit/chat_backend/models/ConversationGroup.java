package vn.edu.iuh.fit.chat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document("ConversationGroup")
@TypeAlias("ConversationGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationGroup extends Conversation{
    private List<User> members;
    private User leaderTeam;
    private List<User> subTeamList;
}
