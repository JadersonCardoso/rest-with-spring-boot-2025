package br.com.jadersoncardoso.controllers;

import br.com.jadersoncardoso.controllers.docs.FileControllerDocs;
import br.com.jadersoncardoso.data.dto.UploadFileResponseDTO;
import br.com.jadersoncardoso.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/files")
public class FileController implements FileControllerDocs {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileStorageService fileStorageService;
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    @PostMapping("/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFile(MultipartFile file) {
        var fileName = this.fileStorageService.storeFile(file);


        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }
    @PostMapping("/uploadMultipleFiles")
    @Override
    public List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files) {
        return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());

    }
    @GetMapping("downloadFile/{fileName:.+}") // .+ , utilizado caso venha o nome com a extensão do arquivo, ex: .pdf
    @Override
    public ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName); //lendo o arquivo
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()); //determina contentType
        } catch (Exception e) {
            logger.error("Could determine file type!");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment=; filename=\"" + resource.getFilename()+ "\"")
                .body(resource);
    }
}
