package com.tracker.taskstracker.storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.tracker.taskstracker.env.EnvironmentGetter;
import com.tracker.taskstracker.exception.TRException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class RemoteFileServiceImpl implements FileService {

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
    public byte[] findFileByName(String filename) {
        try {
            return client.files()
                    .downloadBuilder(insertFrontSlash(filename))
                    .start().getInputStream().readAllBytes();
        } catch (IOException | DbxException e) {
            throw new TRException(e.getMessage(), e);
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
