package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.chat_backend.services.S3Service;

@RestController
@RequestMapping("/s3")
@CrossOrigin("*")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String generateUrl(@RequestParam("file")MultipartFile file) throws IllegalAccessException {
        return s3Service.uploadFile(file);
    }
}
