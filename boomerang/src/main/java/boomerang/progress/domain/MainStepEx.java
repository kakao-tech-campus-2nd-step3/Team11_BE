package boomerang.progress.domain;

import boomerang.progress.dto.SubStepResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class MainStepEx implements MainStep {

    private Boolean subStep1;

    private Boolean subStep2;

    public MainStepEx() {
        this.subStep1 = false;
        this.subStep2 = false;
    }

    public void updateSubStep1(boolean status) {
        this.subStep1 = status;
    }

    public void updateSubStep2(boolean status) {
        this.subStep2 = status;
    }

    @Override

    public String getMainName() {
        return "진행도1번";
    }

    @Override
    public List<SubStepResponseDto> getSubStepAllForResponse() {
        List<SubStepResponseDto> response = new ArrayList<>();
        response.add(new SubStepResponseDto("sub_step1", subStep1));
        response.add(new SubStepResponseDto("sub_step2", subStep2));
        return response;
    }

    @Override
    public Boolean getCompletion() {
        return this.subStep1 && this.subStep2;
    }
}
