package com.tracker.taskstracker.util;

import org.springframework.web.multipart.MultipartFile;

public final class FilesUtil {

    private FilesUtil() {

    }

    public static String getFileExtension(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename()
                        .lastIndexOf("."));
    }
}
