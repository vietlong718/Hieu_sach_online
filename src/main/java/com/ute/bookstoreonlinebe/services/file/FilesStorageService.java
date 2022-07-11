package com.ute.bookstoreonlinebe.services.file;

import com.ute.bookstoreonlinebe.entities.Book;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void init(String folderName);

    void save(String uploadDir, String fileName,  MultipartFile file);

    Resource loadFile(String part);

    String renameFile(String newName, String old);

    void deleteAll();

    Stream<Path> loadAll();

    void deleteAvatar(String folder, String fileName);

    void save(String uploadDir, MultipartFile file);

    void deleteImage(String path);
}
