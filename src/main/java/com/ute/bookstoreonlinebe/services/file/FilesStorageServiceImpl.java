package com.ute.bookstoreonlinebe.services.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Slf4j
@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    @Value("${upload.part}")
    private String uploadFolder;

    private final String currentDirectory = System.getProperty("user.dir");

    @Override
    public void init(String part) {
        Path uploadDir = Paths.get(part);
        try {
            Files.createDirectory(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(String uploadDir, String fileName, MultipartFile file) {
        String part = currentDirectory + uploadFolder + uploadDir;
        Path uploadPart = Paths.get(part);
        try {
            if (!Files.exists(uploadPart)) {
                Files.createDirectories(uploadPart);
            }
            Files.copy(file.getInputStream(), uploadPart.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String filePath) {
        try {
            String uri = currentDirectory + uploadFolder + filePath;
            Path path = Paths.get(uri);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public String renameFile(String newName, String old) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public void deleteAvatar(String folder, String fileName) {
        String path = currentDirectory + uploadFolder + fileName;
        try {
            Path pathFile = Paths.get(path);
            if (Files.exists(pathFile)) {
                Files.delete(pathFile);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void save(String uploadDir, MultipartFile file) {
        String part = currentDirectory + uploadFolder + uploadDir;
        Path uploadPart = Paths.get(part);
        try {
            if (!Files.exists(uploadPart)) {
                Files.createDirectories(uploadPart);
            }
            Files.copy(file.getInputStream(), uploadPart.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteImage(String path) {
        String dlPath = currentDirectory + path;
        try {
            Path pathFile = Paths.get(dlPath);
            if (Files.exists(pathFile)) {
                Files.delete(pathFile);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
