# WisWeb - Word Intelligent System Over the Web

A real-time web chat application built with Java Spring Boot and WebSocket that focuses on security and privacy.

## Features

- No registration required
- Secure chat room creation with CAPTCHA verification
- One-time use join links valid for 10 minutes
- Real-time messaging using WebSocket
- Typing indicators
- Emoji support
- Room expiration notifications
- Messages are not stored on the server
- Chat history is deleted when the session ends

## Technologies Used

- Java 17
- Spring Boot 3.2.1
- Spring WebSocket
- Spring Security
- HTML/CSS/JavaScript
- Bootstrap 5
- SockJS
- STOMP
- Google reCAPTCHA v2

## How It Works

1. A user creates a new chat room by entering their nickname and passing CAPTCHA verification
2. They receive a one-time use link to share with another person
3. When the second user opens the link, they enter their nickname to join the chat
4. Both users can now exchange messages in real-time
5. The chat room automatically expires after 10 minutes
6. No messages are stored on the server

## Setup and Installation

[Installation instructions to be added]

## Configuration

[Configuration details to be added]

## API Endpoints

[List of endpoints to be added]
