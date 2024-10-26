package boomerang.progress.dto;

import lombok.Getter;

@Getter
public class SubStepDto {
    private String name;                  //보험가입여부
    private Boolean completion;


    public SubStepDto(String subStepName, Boolean subStep1) {
        this.name = subStepName;
        this.completion = subStep1;
    }
}
