package com.log.monitoring.service;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Slf4j
public class FileReaderUtil {

    public static Path validatePath(String path){
        Path p= null;
        try {
            log.info("Checking path : "+path);
             p = Path.of(path);
        }catch (InvalidPathException e){
            log.info("This path: {} is an invalid path", path);
            e.printStackTrace();
            throw  e;
        }
        return p;
    }
}
