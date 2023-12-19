package com.log.monitoring.service.configurations;

import com.log.monitoring.service.FileReaderUtil;
import com.log.monitoring.service.models.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.concurrent.Future;

@Component
@Slf4j
public class FileWatcherService {

    private long lastPosition = 0;
    private static long DATA_TRANSFER_LIMIT = 500;
    private Path filePathToBeMonitored;

    SimpMessagingTemplate simpMessagingTemplate;
    FileWatcherService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }



    public void setFilePathToBeMonitored(String filePath) {
        if(this.filePathToBeMonitored!=null)
            log.info("NOTE : FILE PATH IS GETTING RESET from : "+this.filePathToBeMonitored+ " to "+ filePath);
        Path p = FileReaderUtil.validatePath(filePath);
        this.filePathToBeMonitored = p;
        setUpInitialPointers();
        monitorAndPublishFileChanges();
        log.info("Let's see code comes heere or not");




    }

    public Log fetchInitData() {
        StringBuilder sb = new StringBuilder();
        try (FileChannel fileChannel = FileChannel.open(filePathToBeMonitored, StandardOpenOption.READ)) {
            long fileSize = Math.max(0L, fileChannel.size() - DATA_TRANSFER_LIMIT);

            if (fileSize >= 0 && fileSize < lastPosition) {

                fileChannel.position(fileSize);

                ByteBuffer buffer = ByteBuffer.allocate((int) (DATA_TRANSFER_LIMIT));
                while (fileChannel.read(buffer) > 0) {
                    // Process the new content in the buffer (e.g., log it)
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        sb.append((char) buffer.get());
//                        System.out.print();
                    }
                    buffer.clear();
                }
            }
            return new Log(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void monitorAndPublishFileChanges(){
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            filePathToBeMonitored.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);


            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        // New line appended, trigger your method here
                        Path modifiedFilePath = (Path) event.context();
                        String modifiedFileName = modifiedFilePath.toString();
                        Path fullModifiedPath = filePathToBeMonitored.getParent().resolve(modifiedFileName);
                        readNewLines(fullModifiedPath);
                        log.info("New line appended!");
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void triggerOnlyOnFileAppend(){
        System.out.println("Triggering random file analyzer");
    }


    private void setUpInitialPointers() {
        if (this.filePathToBeMonitored==null){
            throw new RuntimeException("Couldn't find the file path properly");
        }
        try{
            Path path = this.filePathToBeMonitored;
            try(FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ) ){
                lastPosition =  fileChannel.size();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void readNewLines(Path filePath) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            long fileSize = fileChannel.size();
            StringBuilder sb = new StringBuilder();
            if (fileSize > lastPosition) {
                fileChannel.position(lastPosition);

                ByteBuffer buffer = ByteBuffer.allocate((int) (fileSize - lastPosition));
                while (fileChannel.read(buffer) > 0) {
                    // Process the new content in the buffer (e.g., log it)
                    buffer.flip();
                    while (buffer.hasRemaining()) {

                        char ch = (char) buffer.get();
                        log.info("Newly appended characters : {}", ch);
                        sb.append(ch);
                    }
                    buffer.clear();
                }

                // Update the last position to the current end of the file
                lastPosition = fileSize;
                this.simpMessagingTemplate.convertAndSend("/topic/updates",new Log(sb.toString()));
            }
        }
    }
}
