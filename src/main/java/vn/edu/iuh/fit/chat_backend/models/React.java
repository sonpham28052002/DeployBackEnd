package vn.edu.iuh.fit.chat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "React")
@TypeAlias("React")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class React {
    @Id
    private User user;
    private String react;
}
