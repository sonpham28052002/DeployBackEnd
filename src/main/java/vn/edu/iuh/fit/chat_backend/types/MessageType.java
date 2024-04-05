package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum MessageType {
    PNG("image/png"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    GIF("image/gif"),
    AUDIO("video/mp3"),
    VIDEO("video/mp4"),
    PDF("application/pdf"),
    DOC("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PPT("application/vnd.ms-powerpoint"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    RAR("application/vnd.rar"),
    JSON("application/json"),
    XML("application/xml"),
    CSV("text/csv"),
    HTML("text/html"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLS("application/vnd.ms-excel"),
    ZIP("application/zip"),
    Text("text/content"),
    TXT("text/plain"),
    STICKER("image/sticker"),
    RETRIEVE("RETRIEVE");
    private final String MessageType;

    MessageType(String type) {
        this.MessageType = type;
    }

}
