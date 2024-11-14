package boomerang.file.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmazonS3ServiceTest {

    @Mock
    private S3Client amazonS3Client;

    @Mock
    private S3Utilities s3Utilities;

    @InjectMocks
    private AmazonS3Service amazonS3Service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(amazonS3Service, "bucket", "test-bucket");
        when(amazonS3Client.utilities()).thenReturn(s3Utilities);
    }

    @Test
    void testUpload_Success() throws IOException {
        // given
        String email = "test@example.com";
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        URL expectedUrl = new URL("https://test-bucket.s3.amazonaws.com/test.jpg");

        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(expectedUrl);
        when(amazonS3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(null);

        // when
        URL result = amazonS3Service.upload(email, file);

        // then
        assertThat(result).isEqualTo(expectedUrl);
        verify(amazonS3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(s3Utilities).getUrl(any(GetUrlRequest.class));
    }

    @Test
    void testPutImage_Success() throws IOException {
        // given
        String fileName = "test/image.jpg";
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        URL expectedUrl = new URL("https://test-bucket.s3.amazonaws.com/test/image.jpg");

        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(expectedUrl);
        when(amazonS3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(null);

        // when
        URL result = amazonS3Service.putImage(fileName, file);

        // then
        assertThat(result).isEqualTo(expectedUrl);
        verify(amazonS3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(s3Utilities).getUrl(any(GetUrlRequest.class));
    }
}
