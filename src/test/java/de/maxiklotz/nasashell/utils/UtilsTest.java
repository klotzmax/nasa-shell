package de.maxiklotz.nasashell.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.maxiklotz.nasashell.configuration.JacksonMapperConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { Utils.class, JacksonMapperConfiguration.class, ObjectMapper.class})
class UtilsTest {

    @Autowired
    private Utils utils;


    @Test
    void testCheckDateFormatWithCorrectDateAndMinus(){
        assertDoesNotThrow(() -> utils.checkDateFormatOrThrow("2023-12-13"));
    }

    @Test
    void testCheckDateFormatWithCorrectOrderButWithDots(){
        assertThrows(IllegalArgumentException.class,() -> utils.checkDateFormatOrThrow("2023.12.13"));
    }

    @Test
    void testCheckDateFormatWithYearOverNineThousandNineHundretAndNinetyNine(){
        assertThrows(IllegalArgumentException.class, () -> utils.checkDateFormatOrThrow("20023-12-13"));
    }

    @Test
    void testCheckDateFormatWithNull(){
        assertThrows(IllegalArgumentException.class,() -> utils.checkDateFormatOrThrow(null));
    }

    @Test
    void testCheckDateFormatWithEmptyString(){
        assertThrows(IllegalArgumentException.class,() -> utils.checkDateFormatOrThrow(null));
    }

    @Test
    void testCheckDateFormatWithDotsGermanOrder(){
        assertThrows(IllegalArgumentException.class,() -> utils.checkDateFormatOrThrow("25.12.2023"));
    }

    @Test
    void testCheckDateFormatWithZeros(){
        assertThrows(IllegalArgumentException.class,() -> utils.checkDateFormatOrThrow("0000-00-00"));
    }

    @Test
    void testCheckDateFormatWithFirstDay(){
        assertDoesNotThrow(() -> utils.checkDateFormatOrThrow("0000-01-01"));
    }


}
