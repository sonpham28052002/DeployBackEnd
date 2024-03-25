package vn.edu.iuh.fit.chat_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.chat_backend.services.AzureStorageService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("azure")
public class AzureController {
    @Autowired
    private AzureStorageService azureStorageServive;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String type =file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        return azureStorageServive.uploadFile(UUID.randomUUID().toString()+"."+type,file.getInputStream(),file.getSize(),file.getContentType());
    }
    @PostMapping("/changeImage")
    public String changeImage(@RequestParam MultipartFile file, @RequestParam String name) throws IOException {
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String nameNew =  name.substring(0,name.lastIndexOf("."));
        return azureStorageServive.uploadFile(nameNew+"."+type,file.getInputStream(),file.getSize(),file.getContentType());
    }
    @GetMapping("/file")
    public String string(){
        return "phạm thanh son";
    }
}
