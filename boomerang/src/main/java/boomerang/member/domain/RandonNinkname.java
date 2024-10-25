package boomerang.member.domain;

import java.text.MessageFormat;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandonNinkname {

    private final String[] ADJECTIVES = {
        "행복한", "똑똑한", "즐거운", "강한", "빠른", "재치있는", "멋진", "훌륭한", "즐거운",
        "아름다운", "기쁜", "사랑스러운", "행복한", "환상적인", "놀라운", "훌륭한", "매력적인", "긍정적인", "빛나는",
        "희망찬", "용감한", "따뜻한", "신나는", "친절한", "든든한", "뛰어난", "성실한", "창의적인",
        "자랑스러운", "유쾌한"
    };

    private final String[] NOUNS = {
        "라이언", "어피치", "프로토", "네오", "무지", "콘", "튜브", "제이지", "춘식이", "죠르디",
        "스카피", "앙몬드", "팬다주니어", "케로", "베로니", "콥", "빠냐"
    };

    private final Random RANDOM = new Random();

    public String generateRandomNickname() {
        // 랜덤 형용사
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];

        // 랜덤 명사
        String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];

        // 생성한 닉네임 반환 (Ex. 놀라운 라이언)
        return MessageFormat.format("{0} {1}", adjective, noun);

    }



}
