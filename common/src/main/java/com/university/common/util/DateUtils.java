package com.university.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatter) : null;
    }

    public static LocalDateTime parse(String dateStr) {
        return dateStr != null ? LocalDateTime.parse(dateStr, formatter) : null;
    }
}
