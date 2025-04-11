package br.com.jadersoncardoso.controllers.docs;

import br.com.jadersoncardoso.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File EndPoint")
public interface FileControllerDocs {

    UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file);
    List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files);
    ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest request);


}
