package boomerang.progress.domain;

import boomerang.progress.dto.SubStepDto;

import java.util.List;

public interface MainStep {
    String getMainName();
    List<SubStepDto> getSubStepAll();
    Boolean getCompletion();
}
