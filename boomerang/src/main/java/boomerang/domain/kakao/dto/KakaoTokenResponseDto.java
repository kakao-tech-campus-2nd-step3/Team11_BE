package boomerang.domain.kakao.dto;

import boomerang.template.domain.TemplateColumn1;
import boomerang.template.domain.TemplateColumn2;
import boomerang.template.domain.TemplateDomain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTokenResponseDto {

    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("expires_in")
    public Integer expiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;
    @JsonProperty("scope")
    public String scope;

//    // 생성자
//    public KakaoTokenResponseDto(Long id, TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
//        this.id = id;
//        this.templateColumn1 = templateColumn1;
//        this.templateColumn2 = templateColumn2;
//    }
//
//    // TemplateCreateServiceDto로 변환하는 메서드
//    public TemplateDomain toTemplateDomain() {
//        return new TemplateDomain(id, templateColumn1, templateColumn2);
//    }
}
