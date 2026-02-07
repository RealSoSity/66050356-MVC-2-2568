package util;

import java.time.LocalDate;

public class DateUtil {
    // Parses a date string in the format "YYYY-MM-DD" to a LocalDate obj
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString); // ISO_LOCAL_DATE format
    }
}
