package vn.edu.iuh.fit.chat_backend.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("account")
@TypeAlias("account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    @Id
    @Indexed
    private String id;
    @Field(name = "phone")
    private String phone;
    @Field(name = "password")
    private String password;
    @Field(name = "createDate")
    private LocalDateTime createDate;
}
