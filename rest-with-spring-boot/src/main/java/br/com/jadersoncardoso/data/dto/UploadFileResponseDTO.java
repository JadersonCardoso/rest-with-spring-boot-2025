package br.com.jadersoncardoso.data.dto;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDonloadUri;
    private String fileType;
    private long size;

    public UploadFileResponseDTO() {
    }

    public UploadFileResponseDTO(String fileName, String fileDonloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDonloadUri = fileDonloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDonloadUri() {
        return fileDonloadUri;
    }

    public void setFileDonloadUri(String fileDonloadUri) {
        this.fileDonloadUri = fileDonloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadFileResponseDTO that = (UploadFileResponseDTO) o;
        return size == that.size && Objects.equals(fileName, that.fileName) && Objects.equals(fileDonloadUri, that.fileDonloadUri) && Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileDonloadUri, fileType, size);
    }
}
