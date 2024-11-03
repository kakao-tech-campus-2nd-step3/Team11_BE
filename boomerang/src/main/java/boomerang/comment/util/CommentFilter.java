package boomerang.comment.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

@Component
public class CommentFilter {
    private BloomFilter<String> profanityFilter;
    private Pattern profanityPattern;

    //욕설이 저장된 json 파일
    @Value("${resources.path.bad-words-file}")
    private String badWordsFilePath;

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
            this.profanityFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 200, 0.01);

            Arrays.sort(profanities, Comparator.comparingInt(String::length).reversed());

            for (String word : profanities) {
                // 단어 경계를 제거하고 직접 매칭
                patternBuilder.append(Pattern.quote(word)).append("|");
                this.profanityFilter.put(Pattern.quote(word));
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_ERROR);
        }

        this.profanityPattern = Pattern.compile(patternBuilder.substring(0, patternBuilder.length() - 1), Pattern.CASE_INSENSITIVE);
    }

    private boolean containsProfanity(String input) {
        for (String word : input.split("\\s+")) { //공백기준으로 자름
            if (profanityFilter.mightContain(word)) {
                return true;
            }
        }
        return false;
    }

    public String filterAndReplaceProfanity(String text) {
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
