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
        corsRegistry.addMapping("/**")
            // 8080 추가
            .allowedOrigins("http://localhost:5173",
                    "http://localhost:8080",
                    "http://54.252.224.76:80",
                    "http://54.252.224.76",
                    "http://52.79.80.3:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)
            .exposedHeaders("Set-Cookie", "Authorization");  // Authorization 헤더 추가
    }
}
