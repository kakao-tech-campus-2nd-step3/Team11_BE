package boomerang.progress.domain;

import boomerang.progress.dto.SubStepResponseDto;

import java.util.List;

public interface MainStep {
    String getMainName();
    List<SubStepResponseDto> getSubStepAllForResponse();
    Boolean getCompletion();
}
