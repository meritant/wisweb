package com.securechat.wisweb.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.securechat.wisweb.model.ChatMessage;
import com.securechat.wisweb.model.ChatMessage.MessageType;
import com.securechat.wisweb.model.ChatRoom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {
    private final Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();
    private static final long ROOM_EXPIRY_MINUTES = 10;

    public ChatRoom createRoom(String hostNickname) {
        ChatRoom room = new ChatRoom(hostNickname);
        chatRooms.put(room.getId(), room);
        System.out.println("Room created - ID: " + room.getId() + ", Token: " + room.getOneTimeToken());
        return room;
    }

    public ChatRoom getRoom(String roomId) {
        ChatRoom room = chatRooms.get(roomId);
        System.out.println("Getting room - ID: " + roomId + ", Found: " + (room != null));
        if (room != null) {
            System.out.println("Room token: " + room.getOneTimeToken());
        }
        
        if (room != null && isRoomExpired(room)) {
            System.out.println("Room expired");
            chatRooms.remove(roomId);
            return null;
        }
        return room;
    }

    private boolean isRoomExpired(ChatRoom room) {
        return room.getCreationTime()
                  .plusMinutes(ROOM_EXPIRY_MINUTES)
                  .isBefore(LocalDateTime.now());
    }

    public boolean joinRoom(String roomId, String guestNickname) {
        ChatRoom room = getRoom(roomId);
        if (room != null && room.getGuestNickname() == null) {
            room.setGuestNickname(guestNickname);
            System.out.println("User " + guestNickname + " joined room " + roomId);
            return true;
        }
        System.out.println("Failed to join room " + roomId);
        return false;
    }

    
    public void removeRoom(String roomId) {
        chatRooms.remove(roomId);
    }
    
    public boolean isTokenValid(String roomId, String token) {
        ChatRoom room = getRoom(roomId);
        System.out.println("Validating token - Room: " + roomId + ", Token: " + token);
        if (room != null) {
            System.out.println("Room token: " + room.getOneTimeToken());
        }
        return room != null && 
               room.getOneTimeToken() != null && 
               room.getOneTimeToken().equals(token);
    }
    
//    Expiration check
    public void checkAndHandleExpiration(String roomId, SimpMessagingTemplate messagingTemplate) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null && isRoomExpired(room)) {
            // Send expiration message to all users in the room
            ChatMessage expirationMessage = new ChatMessage();
            expirationMessage.setType(MessageType.EXPIRED);
            expirationMessage.setRoomId(roomId);
            expirationMessage.setContent("Room has expired");
            
            messagingTemplate.convertAndSend("/topic/room." + roomId, expirationMessage);
            
            // Remove the expired room
            chatRooms.remove(roomId);
        }
    }
}