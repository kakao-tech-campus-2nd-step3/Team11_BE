package boomerang.global.config;

import boomerang.global.utils.MainStepEnumConverter;
import boomerang.global.utils.SubStepEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MainStepEnumConverter());
        registry.addConverter(new SubStepEnumConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        //mvc에서 cors 해결
        corsRegistry.addMapping("/**")
            .exposedHeaders("Set-Cookie")
            .allowedOrigins("http://localhost:3000");
    }
}
