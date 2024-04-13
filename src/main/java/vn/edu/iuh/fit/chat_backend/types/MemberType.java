package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum MemberType {
    GROUP_LEADER("Group Leader"),
    DEPUTY_LEADER("Deputy Leader"),
    MEMBER("Member"),
    LEFT_MEMBER("Left Member");

    private final String roleName;

    MemberType(String roleName) {
        this.roleName = roleName;
    }

}
