package de.maxiklotz.nasashell;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Utils {

    public void checkDateFormat(String date) {
        String regEx = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        final Pattern pattern = Pattern.compile(regEx);
        final Matcher matcher = pattern.matcher(date);
        if(!matcher.matches()){
            throw new IllegalArgumentException("Given date does not match regex: " + regEx);
        }
    }
}
