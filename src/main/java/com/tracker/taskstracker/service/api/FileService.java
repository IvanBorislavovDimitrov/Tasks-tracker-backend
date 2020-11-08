package com.tracker.taskstracker.service.api;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void save(String name, MultipartFile file);

}
