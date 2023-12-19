package com.log.monitoring.service.controllers;


import com.log.monitoring.service.configurations.FileWatcherService;
import com.log.monitoring.service.models.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
public class WebSockController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSockController(SimpMessagingTemplate messagingTemplate) {
        this.simpMessagingTemplate = messagingTemplate;
    }

    @Autowired
    FileWatcherService fileWatcherService;

    @MessageMapping("/init/logs")
    @SendToUser("/queue/updates")
    public Log setUpFileObserver() {
        Log l =  fileWatcherService.fetchInitData();
        log.info("Logs is : {}",l);
        return l;
    }



}
