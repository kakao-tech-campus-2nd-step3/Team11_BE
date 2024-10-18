package boomerang.file.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AmazonS3Service implements FileService {


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Client amazonS3Client;

    public AmazonS3Service(S3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public URL upload(String email, MultipartFile multipartFile) {
        String fileName = String.join("/", email,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SSSSSS")));

        return putImage(fileName, multipartFile);
    }

    public URL putImage(String fileName, MultipartFile multipartFile) {
        try {
            // 업로드할 파일 정보 설정

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .contentLength(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .key(fileName)
                    .build();

            RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            amazonS3Client.putObject(putObjectRequest, requestBody);

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            return amazonS3Client.utilities().getUrl(getUrlRequest);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }
}
