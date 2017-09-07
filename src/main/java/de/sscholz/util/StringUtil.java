package de.sscholz.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    public static final String NEWLINE = "\n";


    public static String currentTimestamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        return sdf.format(date);
    }


    public static int countSubstring(String str, String substr) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(substr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += substr.length();
            }
        }
        return count;
    }
}
