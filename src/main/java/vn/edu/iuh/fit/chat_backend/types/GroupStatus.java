package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum GroupStatus {
    ACTIVE("Active"),
    READ_ONLY("Read Only"),
    DISBANDED("Disbanded"),
    MESSAGE_ONLY("Message Only"),
    CHANGE_IMAGE_ONLY("Change Image Only");

    private final String statusName;

    GroupStatus(String statusName) {
        this.statusName = statusName;
    }

}