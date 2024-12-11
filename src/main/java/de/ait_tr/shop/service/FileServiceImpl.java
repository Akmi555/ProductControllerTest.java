package de.ait_tr.shop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.ait_tr.shop.service.interfaces.FileService;
import de.ait_tr.shop.service.interfaces.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final AmazonS3 client;
    private final ProductService productService;

    public FileServiceImpl(AmazonS3 client, ProductService productService) {
        this.client = client;
        this.productService = productService;
    }

    @Override
    public String upload(MultipartFile file, String productTitle) {
        try {
        // Методанные
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());

        //

           String uniqueFileName = generateUniqueFileName(file);

        //
            PutObjectRequest request = new PutObjectRequest(
                    "cohort-44-bucket",
                    uniqueFileName,
                    file.getInputStream(),
                    metadata
            );

        //
            request.withCannedAcl(CannedAccessControlList.PublicRead);

        //
            client.putObject(request);

            String url = client.getUrl("cohort-44-bucket", uniqueFileName).toString();

        //todo

            productService.attachImage(url, productTitle);


            return url;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //"banana.jpeg"
    //"banana- asfgsag3khl.jpeg"

    private String generateUniqueFileName(MultipartFile file){
        String sourceFilename = file.getOriginalFilename();
        int dotIndex = sourceFilename.lastIndexOf('.');
        String fileName = sourceFilename.substring(0, dotIndex);
        String extension = sourceFilename.substring(dotIndex);

        return String.format("%s-%s%s", fileName, UUID.randomUUID().toString(), extension);


    }



}
