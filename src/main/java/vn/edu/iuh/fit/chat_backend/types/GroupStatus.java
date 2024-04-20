package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum GroupStatus {
    ACTIVE("Active"), // toàn quyền
    READ_ONLY("Read Only"), // chỉ cho phép member đọc tin nhắn
    DISBANDED("Disbanded"), // đã giải tán
    MESSAGE_ONLY("Message Only"), // chi cho member gửi ti nhắn không cho đổi ảnh và tên
    CHANGE_IMAGE_AND_NAME_ONLY("Change Image And Name Only"); // chỉ cho member đổi ảnh và tên

    private final String statusName;

    GroupStatus(String statusName) {
        this.statusName = statusName;
    }

}