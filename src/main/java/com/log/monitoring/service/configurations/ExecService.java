package com.log.monitoring.service.configurations;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecService {

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    ExecutorService executorService;

    ExecService(){
        this.executorService =  Executors.newSingleThreadExecutor();
    }


}
