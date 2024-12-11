package com.securechat.wisweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.securechat.wisweb.config.RecaptchaConfig;
import com.securechat.wisweb.model.ChatRoom;
import com.securechat.wisweb.service.CaptchaService;
import com.securechat.wisweb.service.ChatRoomService;
import com.securechat.wisweb.service.TokenValidationService;

@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private RecaptchaConfig recaptchaConfig;
    
    @Autowired
    private TokenValidationService tokenValidationService;
    
    @GetMapping("/captcha-site-key")
    public String getCaptchaSiteKey() {
        return recaptchaConfig.getSiteKey();
    }

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(
            @RequestParam String nickname,
            @RequestParam("g-recaptcha-response") String captchaResponse) {
        
        try {
            if (!captchaService.validateCaptcha(captchaResponse)) {
                return ResponseEntity.badRequest().body("CAPTCHA validation failed");
            }

            ChatRoom room = chatRoomService.createRoom(nickname);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoom> getRoomInfo(@PathVariable String roomId) {
        ChatRoom room = chatRoomService.getRoom(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.notFound().build();
    }
    
//    @PostMapping("/room/{roomId}/validate")
//    public ResponseEntity<?> validateRoomAccess(
//            @PathVariable String roomId,
//            @RequestParam String token) {
//    	
//    	System.out.println("Validating room access: " + roomId); // Debug log
//        System.out.println("Token: " + token); // Debug log
//        
//        boolean isValid = tokenValidationService.validateToken(roomId, token);
//        
//        if (isValid) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.badRequest().body("Invalid or expired token");
//    }
    
    
    @PostMapping("/room/{roomId}/validate")
    public ResponseEntity<?> validateRoomAccess(
            @PathVariable String roomId,
            @RequestParam String token) {
        
        System.out.println("Received validation request - Room: " + roomId + ", Token: " + token);
        
        boolean isValid = tokenValidationService.validateToken(roomId, token);
        
        if (isValid) {
            System.out.println("Room validation successful");
            return ResponseEntity.ok().build();
        }
        System.out.println("Room validation failed");
        return ResponseEntity.badRequest().body("Invalid or expired token");
    }
    
    
    @PostMapping("/room/{roomId}/join")
    public ResponseEntity<?> joinRoom(
            @PathVariable String roomId,
            @RequestParam String nickname,
            @RequestParam String token) {
    	
    	System.out.println("Joining room: " + roomId); // Debug log
        System.out.println("Nickname: " + nickname); // Debug log
        System.out.println("Token: " + token); // Debug log
        
        if (!chatRoomService.isTokenValid(roomId, token)) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        boolean joined = chatRoomService.joinRoom(roomId, nickname);
        if (joined) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Room not found or already full");
    }
}