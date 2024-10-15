package boomerang.global.utils;

import boomerang.progress.domain.SubStepEnum;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class SubStepEnumConverter implements Converter<String, SubStepEnum> {
    @Override
    public SubStepEnum convert(String value) {
        return SubStepEnum.fromStepName(value);
    }
}
