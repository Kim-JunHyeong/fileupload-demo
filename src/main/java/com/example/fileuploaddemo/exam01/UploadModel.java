package com.example.fileuploaddemo.exam01;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UploadModel {
    private String description;

    private Long category;

    private List<MultipartFile> files;

    public String getDescription() {
        return description;
    }
}
