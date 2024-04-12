package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum GroupStatus {
    ACTIVE("Active"),
    READ_ONLY("Read Only"),
    DISBANDED("Disbanded");

    private final String statusName;

    GroupStatus(String statusName) {
        this.statusName = statusName;
    }

}