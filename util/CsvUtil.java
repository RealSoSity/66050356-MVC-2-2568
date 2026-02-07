package util;

import java.util.ArrayList;
import java.util.List;

// Utility class for CSV operations
public class CsvUtil {
    // Escape special characters in CSV, preparing for CSV output 
    // (Why do this? because "," and "\"" have special meanings in CSV)
    public static String escape(String s) {
        if (s == null)
            return "";
        boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String escaped = s.replace("\"", "\"\"");
        return needQuote ? "\"" + escaped + "\"" : escaped;
    }

    // Parse a CSV line into a list of values for easier processing
    public static List<String> parseLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null || line.isEmpty()) {
            return result;
        }

        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) { // Iterate through each character
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '\"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        cur.append('\"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '\"') {
                    inQuotes = true;
                } else if (c == ',') {
                    result.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        result.add(cur.toString());
        return result;
    }
}
