package com.securechat.wisweb.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.securechat.wisweb.model.ChatMessage;
import com.securechat.wisweb.model.ChatMessage.MessageType;
import com.securechat.wisweb.service.ChatRoomService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/chat.message")
    public void sendMessage(@Payload ChatMessage chatMessage) {
    	
        // Check expiration before processing message
        chatRoomService.checkAndHandleExpiration(chatMessage.getRoomId(), messagingTemplate);
        messagingTemplate.convertAndSend("/topic/room." + chatMessage.getRoomId(), chatMessage);
    }

    @MessageMapping("/chat.join")
    public void joinChat(@Payload ChatMessage chatMessage, 
                        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend("/topic/room." + chatMessage.getRoomId(), chatMessage);
    }
    
    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload ChatMessage chatMessage) {
        chatMessage.setType(MessageType.TYPING);
        messagingTemplate.convertAndSend("/topic/room." + chatMessage.getRoomId(), chatMessage);
    }
}