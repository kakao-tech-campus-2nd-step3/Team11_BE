package boomerang.member.dto;

import lombok.Getter;

@Getter
public class RandomNicknameCreateResponseDTO {
    private String nickname;

    public RandomNicknameCreateResponseDTO() {}

    public RandomNicknameCreateResponseDTO(String nickname) {
        this.nickname = nickname;
    }


}
