package de.maxiklotz.nasashell;

import de.maxiklotz.nasashell.utils.Utils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UtilsTest {

    private final Utils utils = new Utils();

    @Test
    void testCheckDateFormatWithCorrectDate(){
        assertDoesNotThrow(() -> utils.checkDateFormat("2023-12-13"));
    }

    @Test
    void testCheckDateFormatWithWrongDate(){
        assertThrows(IllegalArgumentException.class, () -> utils.checkDateFormat("20023-12-13"));
    }


}
