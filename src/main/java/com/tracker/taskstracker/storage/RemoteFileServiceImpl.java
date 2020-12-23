package com.tracker.taskstracker.storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.tracker.taskstracker.env.EnvironmentGetter;
import com.tracker.taskstracker.exception.TRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class RemoteFileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteFileServiceImpl.class);

    private final EnvironmentGetter environmentGetter;
    private final DbxClientV2 client;

    public RemoteFileServiceImpl(EnvironmentGetter environmentGetter) {
        this.environmentGetter = environmentGetter;
        client = getClient();
    }

    @Override
    public void save(String name, MultipartFile file) {
        try {
            client.files().uploadBuilder(insertFrontSlash(name))
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(file.getInputStream());
        } catch (DbxException | IOException e) {
            throw new TRException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] findFileByName(String name) {
        try {
            return client.files()
                    .downloadBuilder(insertFrontSlash(name))
                    .start().getInputStream().readAllBytes();
        } catch (IOException | DbxException e) {
            throw new TRException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteByName(String name) {
        try {
            client.files()
                    .deleteV2(insertFrontSlash(name));
        } catch (DbxException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    protected DbxClientV2 getClient() {
        DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder(environmentGetter.getDropboxClientIdentifier())
                .build();
        return new DbxClientV2(dbxRequestConfig, environmentGetter.getDropboxAccessToken());
    }

    private String insertFrontSlash(String filename) {
        if (filename == null) {
            return null;
        }
        if (filename.startsWith("/")) {
            return filename;
        }
        return "/" + filename;
    }
}
