package de.maxiklotz.nasashell.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Component
@Slf4j
@AllArgsConstructor
public class Utils {

    private ObjectMapper objectMapper;

    public void checkDateFormatOrThrow(String date) throws IllegalArgumentException {
        if(!StringUtils.hasText(date)){
            throw new IllegalArgumentException("Date mustn't be null or empty");
        }
            objectMapper.convertValue(date, LocalDate.class);
    }
}
