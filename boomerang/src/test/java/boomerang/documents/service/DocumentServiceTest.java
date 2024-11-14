package boomerang.documents.service;

import boomerang.documents.dto.DocumentRequestDto;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.service.SubStepInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private S3Client s3Client;
    @Mock
    private SubStepInfoService subStepInfoService;
    @InjectMocks
    private DocumentService documentService;

    private DocumentRequestDto requestDto;
    private Map<String, String> formData;
    private byte[] templatePdf;
    private List<String> subStepInputs;
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("BC1_1_\\d{8}_\\d{6}\\.pdf");

    @BeforeEach
    void setUp() {
        formData = Map.of("field1", "value1", "field2", "value2");
        subStepInputs = List.of("field1", "field2");
        requestDto = new DocumentRequestDto(SubStepEnum.BC1_1.getSubStepName(), formData);
        templatePdf = "%PDF-1.4\n...".getBytes(); // 유효한 PDF 데이터
    }

    @Test
    void testGenerateDocument_ValidationFailed_MissingFields() {
        // given
        Map<String, String> incompleteFormData = Map.of("field1", "value1");
        DocumentRequestDto incompleteRequest = new DocumentRequestDto(SubStepEnum.BC1_1.getSubStepName(), incompleteFormData);
        when(subStepInfoService.getSubStepInfo(any(SubStepEnum.class))).thenReturn(subStepInputs);

        // when, then
        assertThatThrownBy(() -> documentService.generateDocument(incompleteRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DOCUMENT_MISSING_FIELDS.getMessage());
    }

    @Test
    void testGenerateDocument_S3DownloadFailed() {
        // given
        when(subStepInfoService.getSubStepInfo(any(SubStepEnum.class))).thenReturn(subStepInputs);
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(new RuntimeException("S3 Error"));

        // when, then
        assertThatThrownBy(() -> documentService.generateDocument(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.S3_UPLOAD_ERROR.getMessage());
    }
}