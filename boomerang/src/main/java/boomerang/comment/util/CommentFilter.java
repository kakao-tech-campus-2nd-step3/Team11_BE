package boomerang.comment.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CommentFilter {

    //BloomFilter : 속도가 빨라 대규모 데이터 검사에 적절한 자료구조
    private BloomFilter<String> profanityFilter;
    private Pattern profanityPattern;

    //욕설이 저장된 json 파일
    @Value("${resources.path.bad-words-file}")
    private String badWordsFilePath;

    //전화번호의 정규 표현식
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
        "(\\d{2,4}[-.\\s]?\\d{3,4}[-.\\s]?\\d{4})|" + // 일반 전화번호 형식
            "(\\(\\d{2,3}\\)[-\\s]?\\d{3,4}[-\\s]?\\d{4})" // 지역번호가 괄호로 묶인 형식
    );


    @PostConstruct
    public void init() {
        ClassPathResource resource = new ClassPathResource(badWordsFilePath);
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder patternBuilder = new StringBuilder();

        try (InputStream inputStream = resource.getInputStream()) {
            String[] profanities = mapper.readValue(inputStream, String[].class);

            /*
             * 필터링할 데이터의 특징
             * funnel –  구축된 BloomFilter가 사용할 T의 퍼널 (funnel은 입력방식을 지정하는 인터페이스)
             *           여기서는 UTF_8 타입의 데이터를 바이트 배열로 변환하여 BloomFilter에 입력할 수 있도록 하는 역할
             * ExpectInsertions – 구축된 BloomFilter에 예상되는 삽입 수
             * fpp - 원하는 거짓양성 확률(양수여야 하고 1.0보다 작아야 함)
             */

            this.profanityFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
                200, 0.01);

            Arrays.sort(profanities, Comparator.comparingInt(String::length).reversed());

            for (String word : profanities) {
                // 1. 단어 경계를 제거, 정규표현식에서 or의 뜻을 가지는 `|` 을 추가
                // 2. profanityFilter에 필터링할 데이터를 추가
                patternBuilder.append(Pattern.quote(word)).append("|");
                this.profanityFilter.put(Pattern.quote(word));
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_ERROR);
        }

        this.profanityPattern = Pattern.compile(
            patternBuilder.substring(0, patternBuilder.length() - 1), Pattern.CASE_INSENSITIVE);
    }

    private boolean containsProfanity(String input) {
        for (String word : input.split("\\s+")) { //공백기준으로 자름
            //word가 검사하고자 하는 데이터를 가지고 있는지를 리턴
            if (profanityFilter.mightContain(word)) {
                return true;
            }
        }
        return false;
    }

    public String filterAndReplaceProfanity(String text) {

        /*
         * [댓글 필터링 방법]
         * 1. 속도가 빠른 BloomFilter로 해당 댓글이 욕설을 포함하고 있는지를 검사
         * 2. 포함하고 있을 경우에만 정규 표현식을 이용해 욕설을 ***로 변경
         */

        if (containsProfanity(text)) {
            return profanityPattern.matcher(text).replaceAll("***");
        }
        return text;
    }


    // 전화번호 형식 포함 여부를 검사하는 메서드
    public boolean containsPhoneNumber(String input) {
        return PHONE_NUMBER_PATTERN.matcher(input).find();
    }
}
