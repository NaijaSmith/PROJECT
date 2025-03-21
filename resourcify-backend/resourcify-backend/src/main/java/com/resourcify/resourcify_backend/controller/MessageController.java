package com.resourcify.resourcify_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        System.out.println("Received message: " + message);
        return "Server received: " + message;
    }
}
// Compare this snippet from src/main/resources/application.properties: