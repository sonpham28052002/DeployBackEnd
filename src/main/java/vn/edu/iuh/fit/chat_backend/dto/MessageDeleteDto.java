package vn.edu.iuh.fit.chat_backend.dto;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.chat_backend.models.Message;
@Document(collection = "MessageDeleteDto")
@TypeAlias("MessageDeleteDto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MessageDeleteDto {
    private Message message;
    private String idGroup;
}
