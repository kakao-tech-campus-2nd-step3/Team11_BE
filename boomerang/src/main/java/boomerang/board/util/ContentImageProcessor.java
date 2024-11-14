package boomerang.board.util;

import boomerang.board.dto.BoardRequestDto;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;

import java.net.URL;
import java.util.List;

public class ContentImageProcessor {
    static public void insertImageUrlsIntoContent(BoardRequestDto boardRequestDto, List<URL> imageUrls) {
        String content = boardRequestDto.getContent();

        // content 내의 이미지 태그 개수 계산
        int contentImageCount = countImagePlaceholders(content);
        int providedImageCount = imageUrls.size();

        // 이미지 태그 개수와 실제 이미지 개수가 불일치하면 에러 발생
        if (contentImageCount != providedImageCount) {
            throw new BusinessException(ErrorCode.IMAGE_COUNT_MISMATCH_ERROR);
        }

        // <img src=?> 부분을 imageUrls의 URL로 순서대로 대체
        for (URL imageUrl : imageUrls) {
            content = content.replaceFirst("<img src=\\? />",
                    "<img src='" + imageUrl.toString() + "' />");
        }

        boardRequestDto.setContent(content);
    }

    static public int countImagePlaceholders(String content) {
        int count = 0;
        int index = 0;

        // "<img src=? />" 패턴을 찾아서 카운트
        while ((index = content.indexOf("<img src=? />", index)) != -1) {
            count++;
            index += "<img src=? />".length();
        }

        return count;
    }
}
