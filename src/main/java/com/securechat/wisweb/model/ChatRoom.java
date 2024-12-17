package com.securechat.wisweb.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.securechat.wisweb.service.ChatRoomService;

@Data
public class ChatRoom {
    private final String id;
    private String oneTimeToken;
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime creationTime;
    private boolean isActive;
    private String hostNickname;
    private String guestNickname;
    private final long expiryMinutes;
    

    public ChatRoom(String hostNickname) {
        this.id = UUID.randomUUID().toString();
        this.oneTimeToken = UUID.randomUUID().toString();
        this.creationTime = LocalDateTime.now();
        this.isActive = true;
        this.hostNickname = hostNickname;
        this.expiryMinutes = ChatRoomService.getRoomExpiryMinutes();

    }
}