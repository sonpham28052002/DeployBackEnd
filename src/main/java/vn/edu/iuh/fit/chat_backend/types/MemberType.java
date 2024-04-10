package vn.edu.iuh.fit.chat_backend.types;

public enum MemberType {
    ACTIVE("active"),
    INACTIVE("inactive");
    private final String type;

    MemberType(String type) {
        this.type= type;
    }
}
