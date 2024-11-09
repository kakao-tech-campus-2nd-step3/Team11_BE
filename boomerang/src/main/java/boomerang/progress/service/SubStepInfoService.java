package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.MainStep;
import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.domain.SubStepInfo;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.repository.SubStepInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubStepInfoService {

    private final SubStepInfoRepository subStepInfoRepository;

    public List<String> getSubStepInfo(SubStepEnum subStepEnum) {
        SubStepInfo subStepInfo = subStepInfoRepository.findBySubStepEnum(subStepEnum)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUB_STEP_INFO_NOT_FOUND));

        return subStepInfo.getInputs();
    }

    public String getSubStepContent(SubStepEnum subStepEnum) {
        SubStepInfo subStepInfo = subStepInfoRepository.findBySubStepEnum(subStepEnum)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUB_STEP_INFO_NOT_FOUND));

        return subStepInfo.getContent();
    }

    public List<SubStepResponseDto> getSubStepList(MainStep mainStep) {
        return mainStep.getSubStepList()
                .stream()
                .map(subStep -> new SubStepResponseDto(subStep, getSubStepContent(subStep.getSubStepEnum())))
                .toList();
    }
}
