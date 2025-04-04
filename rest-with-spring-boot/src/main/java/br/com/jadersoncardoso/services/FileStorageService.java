package br.com.jadersoncardoso.services;

import br.com.jadersoncardoso.config.FileStorageConfig;
import br.com.jadersoncardoso.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath()
                .toAbsolutePath().normalize();

        this.fileStorageLocation = path;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); //limpa o nome de caracteres inválidos ?*
        try {
            if (fileName.contains("..")) throw new FileStorageException("Sorry! Filename Contains a Invalid path sequence " + fileName);
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try Again!", e);
        }
    }
}
