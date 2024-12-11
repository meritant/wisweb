package com.securechat.wisweb.service;

import org.springframework.stereotype.Service;
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
        return room;
    }

    public ChatRoom getRoom(String roomId) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null && isRoomExpired(room)) {
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
            return true;
        }
        return false;
    }
}