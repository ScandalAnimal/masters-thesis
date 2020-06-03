package cz.vutbr.fit.maros.dip.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration {

    private static final String[] CORS_ALLOWED_METHODS = {
            RequestMethod.GET.toString(),
            RequestMethod.POST.toString(),
            RequestMethod.PUT.toString(),
            RequestMethod.DELETE.toString(),
            RequestMethod.PATCH.toString()};

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*").allowedMethods(CORS_ALLOWED_METHODS);
            }
        };
    }
}
