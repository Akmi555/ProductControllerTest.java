package de.ait_tr.shop.service.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {
    String upload(MultipartFile file, String productTitle);
}
