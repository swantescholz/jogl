package de.sscholz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
    public static String readFile(File file) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            System.err.println(e);
        }
        return "";
    }
}
