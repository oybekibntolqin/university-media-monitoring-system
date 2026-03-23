package uz.otfiv.universitymediamonitoringsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Match all paths
                .allowedOrigins("*")  // Allow requests from any origin
                .allowedHeaders("*")  // Allow all headers
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");  // Allow these HTTP methods
    }
}
