package com.tracker.taskstracker.env;

import org.springframework.stereotype.Component;

@Component
public class EnvironmentGetter {

    private static final String FILE_STORAGE_TYPE_NAME = "FILE_STORAGE_TYPE";
    private static final String DROPBOX_ACCESS_TOKEN_NAME = "DROPBOX_ACCESS_TOKEN";
    private static final String DROPBOX_CLIENT_IDENTIFIER_NAME = "DROPBOX_CLIENT_IDENTIFIER_NAME";

    private static final String DEFAULT_FILE_STORAGE_TYPE_VALUE = "REMOTE";
    private static final String DROPBOX_CLIENT_IDENTIFIER_VALUE = "dropbox/learn-to-code";

    public String getFileStorage() {
        return getOrDefault(FILE_STORAGE_TYPE_NAME, DEFAULT_FILE_STORAGE_TYPE_VALUE);
    }

    public String getDropboxClientIdentifier() {
        return getOrDefault(DROPBOX_CLIENT_IDENTIFIER_NAME, DROPBOX_CLIENT_IDENTIFIER_VALUE);
    }

    public String getDropboxAccessToken() {
        return getOrDefault(DROPBOX_ACCESS_TOKEN_NAME, null);
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrDefault(String name, T defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            return defaultValue;
        }
        if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(value);
        } else if (defaultValue instanceof String) {
            return (T) value;
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(value);
        } else if (defaultValue instanceof Double) {
            return (T) Double.valueOf(value);
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(value);
        }
        return (T) value;
    }

}
