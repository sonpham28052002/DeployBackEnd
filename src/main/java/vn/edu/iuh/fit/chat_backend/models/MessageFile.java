package vn.edu.iuh.fit.chat_backend.models;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MessageFile")
@TypeAlias("MessageFile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageFile extends Message{
    private float size;
    private String titleFile;
    private String url;
}
