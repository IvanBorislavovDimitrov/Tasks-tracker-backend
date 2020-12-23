package com.tracker.taskstracker.storage;

import com.tracker.taskstracker.env.EnvironmentGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileStorageGetter {

    private final EnvironmentGetter environmentGetter;

    @Autowired
    public FileStorageGetter(EnvironmentGetter environmentGetter) {
        this.environmentGetter = environmentGetter;
    }

    public FileService getFileService() {
        return FileStorageType.getFileStorageType(environmentGetter.getFileStorage())
                .getFileService(environmentGetter);
    }

}
