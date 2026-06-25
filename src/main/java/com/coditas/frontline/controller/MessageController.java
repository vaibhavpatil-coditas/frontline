package com.coditas.frontline.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/v1/tickets/1/chat")
    @SendTo("/topic/messages")
    public String sendMessage(){
        log.info("Messaged received");
        return "";
    }
}
