package com.log.monitoring.service.controllers;

import com.log.monitoring.service.FileReaderUtil;
import com.log.monitoring.service.configurations.*;
import com.log.monitoring.service.configurations.FileWatcherService;
import com.log.monitoring.service.models.SetFilePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@EnableWebMvc
@Slf4j
@RestController
public class HttpController {


    @Autowired
    FileWatcherService fileWatcherService;


    @Autowired
    ExecService execService;


    List<Future> futures;
    HttpController(){
        this.futures = new ArrayList<>();
    }


    @PostMapping("/set-path")
    public String setFilePath(@RequestBody SetFilePathRequest body) throws Exception{
        System.out.println(" Payload is : "+body.getPath());
        if(futures.size()>0){
            futures.forEach(future -> {
                future.cancel(true);
            });
            futures = new ArrayList<>();
        }
        Future<?>  fut = execService.getExecutorService().submit(()-> {
            fileWatcherService.setFilePathToBeMonitored(body.getPath());
        });
        futures.add(fut);
        return body.getPath();
    }

}
