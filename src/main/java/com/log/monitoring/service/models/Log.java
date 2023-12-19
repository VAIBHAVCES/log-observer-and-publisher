package com.log.monitoring.service.models;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class Log implements Serializable {

    String content;

    public Log(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
