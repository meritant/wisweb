package com.securechat.wisweb.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.securechat.wisweb.model.ChatRoom;
import java.time.LocalDateTime;

@Service
public class TokenValidationService {
    
    @Autowired
    private ChatRoomService chatRoomService;

    public boolean validateToken(String roomId, String token) {
        System.out.println("Starting token validation for room: " + roomId);
        System.out.println("Provided token: " + token);
        
        ChatRoom room = chatRoomService.getRoom(roomId);
        
        if (room == null) {
            System.out.println("Room not found: " + roomId);
            return false;
        }

        if (room.getOneTimeToken() == null) {
            System.out.println("Room token is null");
            return false;
        }

        System.out.println("Stored room token: " + room.getOneTimeToken());

        // Check if token matches
        if (!room.getOneTimeToken().equals(token)) {
            System.out.println("Token mismatch");
            return false;
        }

        // Check if room is already full
        if (room.getGuestNickname() != null) {
            System.out.println("Room is full");
            return false;
        }

        // Check if token is expired (10 minutes)
        if (room.getCreationTime().plusMinutes(10).isBefore(LocalDateTime.now())) {
            System.out.println("Token expired");
            chatRoomService.removeRoom(roomId);
            return false;
        }

        System.out.println("Token validated successfully");
        return true;
    }
}