package com.log.monitoring.service;

import com.log.monitoring.service.configurations.FileWatcherService;
import com.log.monitoring.service.models.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

@Slf4j
@Component
public class WebSocketEventListener {


    @Autowired
    FileWatcherService fileWatcherService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.simpMessagingTemplate = messagingTemplate;
    }


}
