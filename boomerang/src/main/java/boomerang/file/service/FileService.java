package boomerang.file.service;

import java.net.URL;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

    URL upload(String email, MultipartFile multipartFile);
}
