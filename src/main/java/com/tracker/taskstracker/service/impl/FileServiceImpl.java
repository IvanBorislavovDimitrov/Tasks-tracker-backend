package com.tracker.taskstracker.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.service.api.FileService;

@Service
public class FileServiceImpl implements FileService {

    private final String FILES_DIRECTORY = System.getProperty("user.home") + File.separator + "files";

    @Override
    public void save(String name, MultipartFile file) {
        File directory = new File(FILES_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        byte[] bytes = getBytes(file);
        saveFile(name, bytes);
    }

    @Override
    public byte[] findImageByName(String projectPictureName) {
        File file = new File(FILES_DIRECTORY + File.separator + projectPictureName);
        if (!file.exists()) {
            throw new TRException("Image not found");
        }
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new TRException("Image not loaded");
        }
    }

    private byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new TRException(e.getMessage(), e);
        }
    }

    private void saveFile(String name, byte[] bytes) {
        try {
            FileUtils.writeByteArrayToFile(new File(FILES_DIRECTORY + File.separator + name), bytes);
        } catch (IOException e) {
            throw new TRException(e.getMessage(), e);
        }
    }

}
