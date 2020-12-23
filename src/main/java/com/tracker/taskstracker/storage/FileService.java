package com.tracker.taskstracker.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void save(String name, MultipartFile file);

    byte[] findFileByName(String name);

    void deleteByName(String name);
}
