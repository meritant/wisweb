package com.securechat.wisweb.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatRoom {
    private final String id;
    private String oneTimeToken;
    private final LocalDateTime creationTime;
    private boolean isActive;
    private String hostNickname;
    private String guestNickname;

    public ChatRoom(String hostNickname) {
        this.id = UUID.randomUUID().toString();
        this.oneTimeToken = UUID.randomUUID().toString();
        this.creationTime = LocalDateTime.now();
        this.isActive = true;
        this.hostNickname = hostNickname;
    }
}