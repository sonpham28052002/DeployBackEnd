package vn.edu.iuh.fit.chat_backend.services;


import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureStorageService {
    @Value("${azure.connection}")
    private String connection;


    @Value("${azure.container}")
    private String container;

    private BlobContainerClient containerClient() {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connection).buildClient();
        return serviceClient.getBlobContainerClient(container);
    }

    public String uploadFile(String fileName, InputStream inputStream, long length, String type) {
        BlobClient client = containerClient().getBlobClient(fileName);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(type);
        BlobParallelUploadOptions options = new BlobParallelUploadOptions(inputStream,length)
                .setHeaders(headers);
        client.uploadWithResponse(options, null, Context.NONE);
        return client.getBlobUrl();
    }

}
