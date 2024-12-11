package de.ait_tr.shop.controller;

import de.ait_tr.shop.exception_handling.Response;
import de.ait_tr.shop.service.interfaces.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileControler {

    private final FileService fileService;

    public FileControler(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Response upload(MultipartFile file, String productTitle){

        String url = fileService.upload(file, productTitle);

        return new Response("file upload! Url: " + url);
    }
}
