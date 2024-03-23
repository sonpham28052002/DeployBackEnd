package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum ConversationType {
    group("group"),
    single("single");
    private final String conversationType;
    ConversationType(String type) {
        this.conversationType = type;
    }
}
