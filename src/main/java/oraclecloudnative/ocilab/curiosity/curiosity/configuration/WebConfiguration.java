package oraclecloudnative.ocilab.curiosity.curiosity.configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        //registry.addMapping("/**");
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*");
       // registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*").allowedHeaders("*");
       //registry.addMapping("/**").allowedOrigins("http://192.168.1.59:3000", "http://localhost:3000", "http://localhost:31811","http://mydesktop.info:31811", "http://192.168.1.61:31811"); //consider admiting only origins from the LoadBalancer or Ingress Controller
    }
}
