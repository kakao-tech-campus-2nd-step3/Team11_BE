package boomerang.global.utils;

import boomerang.progress.domain.MainStepEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MainStepEnumConverter implements Converter<String, MainStepEnum> {
    @Override
    public MainStepEnum convert(String value) {
        return MainStepEnum.fromStepName(value);
    }
}
