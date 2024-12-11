package com.securechat.wisweb.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;
    private String content;
    private String sender;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE, TYPING
    }
}