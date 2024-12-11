package com.securechat.wisweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.securechat.wisweb.model.ChatRoom;
import com.securechat.wisweb.service.ChatRoomService;

@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestParam String nickname) {
        // Note: CAPTCHA validation will be added here later
        ChatRoom room = chatRoomService.createRoom(nickname);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/room/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, 
                                    @RequestParam String nickname) {
        boolean joined = chatRoomService.joinRoom(roomId, nickname);
        if (joined) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Room not found or already full");
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoom> getRoomInfo(@PathVariable String roomId) {
        ChatRoom room = chatRoomService.getRoom(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.notFound().build();
    }
}