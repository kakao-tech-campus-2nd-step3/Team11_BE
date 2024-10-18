package boomerang.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Service
public interface FileService {
    URL upload(String email, MultipartFile multipartFile);
}
