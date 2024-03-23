package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;
import org.springframework.web.bind.annotation.RestController;

@Getter
public enum Gender {
    Nam("nam"),
    Nữ("nữ");
    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
