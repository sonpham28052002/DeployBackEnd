package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum Emotion {
    SAD("Sad"),
    HAPPY("Haha"),
    ANGRY("Angry"),
    LIKE("LIKE"),
    HEART("Heart");
    private final String description;

    Emotion(String description) {
        this.description = description;
    }

}
