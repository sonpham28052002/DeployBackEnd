package vn.edu.iuh.fit.chat_backend.types;

import lombok.Getter;

@Getter
public enum MessageType {
    PNG("image/png"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    GIF("image/gif"),
    MP3("video/mp3"),
    MP4("video/mp4"),
    PDF("application/pdf"),
    DOC("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PPT("application/vnd.ms-powerpoint"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    RAR("application/vnd.rar"),
    ZIP("application/zip"),
    Text("text/content"),
    STICKER("image/sticker"),
    RETRIEVE("RETRIEVE");
    private final String MessageType;

    MessageType(String type) {
        this.MessageType = type;
    }

}
