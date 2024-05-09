package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum NotificationType {
    CREATE_GROUP("CREATE_GROUP"),
    JOIN_CALL("JOIN_CALL"),
    ADD_MEMBER("ADD_MEMBER"),
    OUT_GROUP("OUT_GROUP"),
    GET_OUT_GROUP("GET_OUT_GROUP");

    private final String notify;

    NotificationType(String notify) {
        this.notify = notify;
    }
}
