package vn.edu.iuh.fit.chat_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.chat_backend.models.User;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String id;
    private MessageType messageType;
    private User sender;
    private User receiver;
    private String content;
    private float size;
    private String titleFile;
    private String url;
}
