package com.tracker.taskstracker.storage;

import com.tracker.taskstracker.env.EnvironmentGetter;

import java.text.MessageFormat;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FileStorageType {

    LOCAL {
        @Override
        public FileService getFileService(EnvironmentGetter environmentGetter) {
            return new LocalFileServiceImpl();
        }
    }, REMOTE {
        @Override
        public FileService getFileService(EnvironmentGetter environmentGetter) {
            return new RemoteFileServiceImpl(environmentGetter);
        }
    };

    public static final Map<String, FileStorageType> NAMES_TO_VALUES = Stream.of(values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static FileStorageType getFileStorageType(String fileStorageKey) {
        FileStorageType fileStorageType = NAMES_TO_VALUES.get(fileStorageKey);
        if (fileStorageType == null) {
            throw new IllegalStateException(MessageFormat.format("Invalid type of file storage: \"{0}\"", fileStorageKey));
        }
        return fileStorageType;
    }

    public abstract FileService getFileService(EnvironmentGetter environmentGetter);

}
