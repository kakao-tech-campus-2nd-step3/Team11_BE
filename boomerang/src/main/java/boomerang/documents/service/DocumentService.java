package boomerang.documents.service;

import boomerang.documents.dto.DocumentRequestDto;
import boomerang.documents.dto.DocumentResponseDto;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.service.SubStepInfoService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final S3Client s3Client;
    private static final float DPI = 300; // 이미지 해상도

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public DocumentResponseDto generateDocument(DocumentRequestDto requestDto) {
        try {
            requestDto.validate();
            byte[] templatePdf = downloadTemplate(requestDto.getSubStep());
            byte[] generatedPdf = fillPdfForm(templatePdf, requestDto.getFormData());

            return new DocumentResponseDto(generatedPdf, requestDto.getSubStep());
        } catch (IOException e) {
            log.error("Failed to process PDF document", e);
            throw new BusinessException(ErrorCode.DOCUMENT_GENERATION_FAILED);
        }
    }

    private byte[] fillPdfForm(byte[] templatePdf, Map<String, String> formData)
        throws IOException {
        byte[] filledPdf;
        try (PDDocument document = Loader.loadPDF(templatePdf)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                log.error("No AcroForm found in the document");
                throw new BusinessException(ErrorCode.DOCUMENT_GENERATION_FAILED);
            }

            // 폰트 설정
            PDFont koreanFont = PDType0Font.load(document, new File("C:/Windows/Fonts/malgun.ttf"));
            PDResources resources = Optional.ofNullable(acroForm.getDefaultResources())
                .orElseGet(PDResources::new);
            resources.getCOSObject().setItem(COSName.FONT, new COSDictionary());
            resources.put(COSName.getPDFName("KoreanFont"), koreanFont);
            acroForm.setDefaultResources(resources);

            // 폼 데이터 채우기
            formData.forEach((fieldName, fieldValue) -> {
                if (fieldValue == null || fieldValue.trim().isEmpty()) {
                    return; // 값이 없는 필드는 건너뛰기
                }
                try {
                    if (fieldName.endsWith("*")) {
                        // 체크박스 필드 처리
                        handleCheckboxField(acroForm, fieldName, fieldValue);
                    } else {
                        // 일반 텍스트 필드 처리
                        PDField field = acroForm.getField(fieldName);
                        if (field instanceof PDTextField textField) {
                            // 기존 default appearance string에서 폰트 크기 추출
                            String defaultAppearance = textField.getDefaultAppearance();
                            String fontSize = "12";

                            if (defaultAppearance != null && !defaultAppearance.isEmpty()) {
                                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                                    "\\s+(\\d+(\\.\\d+)?)\\s+Tf\\s+");
                                java.util.regex.Matcher matcher = pattern.matcher(
                                    defaultAppearance);
                                if (matcher.find()) {
                                    fontSize = matcher.group(1);
                                }
                            }

                            textField.setDefaultAppearance("/KoreanFont " + fontSize + " Tf 0 g");
                            textField.setValue(fieldValue);
                        } else if (field != null) {
                            field.setValue(fieldValue);
                        }
                    }
                } catch (IOException e) {
                    log.error("Error setting value for field: {} (value: {})", fieldName,
                        fieldValue, e);
                }
            });

            acroForm.flatten();

            ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
            document.save(tempBaos);
            filledPdf = tempBaos.toByteArray();
        }

        // PDF를 이미지로 변환
        List<BufferedImage> images = new ArrayList<>();
        try (PDDocument document = Loader.loadPDF(filledPdf)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, DPI);
                images.add(image);
            }
        }

        // 이미지를 새 PDF로 변환
        try (PDDocument newDocument = new PDDocument()) {
            for (BufferedImage image : images) {
                PDPage page = new PDPage(PDRectangle.A4);
                newDocument.addPage(page);

                PDImageXObject pdImage = LosslessFactory.createFromImage(newDocument, image);

                try (PDPageContentStream contentStream = new PDPageContentStream(newDocument,
                    page)) {
                    float scale = Math.min(
                        page.getMediaBox().getWidth() / image.getWidth(),
                        page.getMediaBox().getHeight() / image.getHeight()
                    );

                    float width = image.getWidth() * scale;
                    float height = image.getHeight() * scale;

                    float x = (page.getMediaBox().getWidth() - width) / 2;
                    float y = (page.getMediaBox().getHeight() - height) / 2;

                    contentStream.drawImage(pdImage, x, y, width, height);
                }
            }

            ByteArrayOutputStream finalBaos = new ByteArrayOutputStream();
            newDocument.save(finalBaos);
            return finalBaos.toByteArray();
        }
    }

    private void handleCheckboxField(PDAcroForm acroForm, String fieldName, String fieldValue)
        throws IOException {
        // 선택지 파싱
        String[] options = parseOptions(fieldName);

        // 각 옵션에 대한 체크박스 처리
        for (String option : options) {
            String checkboxFieldName = fieldName + "_" + option;
            PDField field = acroForm.getField(checkboxFieldName);

            if (field instanceof PDCheckBox checkbox) {
                if (option.equals(fieldValue)) {
                    String[] exportValues = checkbox.getExportValues().toArray(new String[0]);
                    if (exportValues.length > 0) {
                        checkbox.setValue(exportValues[0]);
                    } else {
                        checkbox.setValue("Yes");
                    }
                } else {
                    checkbox.setValue("Off");
                }
            }
        }
    }

    private String[] parseOptions(String fieldName) {
        int start = fieldName.indexOf("(");
        int end = fieldName.indexOf(")");
        if (start >= 0 && end >= 0) {
            String optionsStr = fieldName.substring(start + 1, end);
            return optionsStr.split("/");
        }
        return new String[0];
    }

    private byte[] downloadTemplate(String templateName) throws IOException {
        try {
            log.info("Downloading template: {}", templateName);
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(templateName + ".pdf")
                .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            return IOUtils.toByteArray(response);
        } catch (Exception e) {
            log.error("Failed to download template from S3: {}", templateName, e);
            throw new BusinessException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }
}