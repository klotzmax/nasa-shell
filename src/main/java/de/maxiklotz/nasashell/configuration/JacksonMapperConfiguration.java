package de.maxiklotz.nasashell.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class JacksonMapperConfiguration {

    private ObjectMapper objectMapper;

    @PostConstruct
    void init(){
        objectMapper.registerModule(new JavaTimeModule());
    }

}
