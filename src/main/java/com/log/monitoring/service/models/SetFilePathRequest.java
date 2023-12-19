package com.log.monitoring.service.models;

import org.springframework.web.bind.annotation.PostMapping;

public class SetFilePathRequest {
    String path;
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



}
