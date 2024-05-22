package me.cyh2.html;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HtmlReader {
    public static StringBuilder LoadHtmlFile (String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            StringBuilder file = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                file.append(line).append("\n");
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new StringBuilder();
    }
}
