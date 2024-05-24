package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum NotificationType {
    CREATE_GROUP("CREATE_GROUP"),
    JOIN_CALL("JOIN_CALL"),
    ADD_MEMBER("ADD_MEMBER"),
    OUT_CALL("OUT_CALL"),
    CHANGE_ROLE("CHANGE_ROLE"),
    GET_OUT_GROUP("GET_OUT_GROUP"),
    OUT_GROUP("OUT_GROUP"),
    UPDATE_GROUP("UPDATE_GROUP"),
    LEADER_OUT_GROUP("LEADER_OUT_GROUP");


    private final String notify;

    NotificationType(String notify) {
        this.notify = notify;
    }
}
