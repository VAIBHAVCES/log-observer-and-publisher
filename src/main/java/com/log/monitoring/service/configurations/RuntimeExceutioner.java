package com.log.monitoring.service.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RuntimeExceutioner implements CommandLineRunner {

//    @Autowired
//    FileWatcherService fileWatcherService;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Starting as boot start time ........");
//        fileWatcherService.monitorFileForChange();
    }
}
