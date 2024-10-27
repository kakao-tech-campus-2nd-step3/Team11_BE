package boomerang.member.dto;

import lombok.Getter;

@Getter
public class NicknameUpdateRequestDto {
    private String newNickname;

    public String getNewNickname() {
        return newNickname;
    }

    public void setNewNickname(String newNickname) {
        this.newNickname = newNickname;
    }
}
