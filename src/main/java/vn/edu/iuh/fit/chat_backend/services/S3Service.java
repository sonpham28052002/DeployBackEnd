package vn.edu.iuh.fit.chat_backend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3Service {
    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileType = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
        String key = UUID.randomUUID().toString();
        System.out.println(fileType);
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key + "." + fileType)
                            .contentType(fileType)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toString() + "." + fileType;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
