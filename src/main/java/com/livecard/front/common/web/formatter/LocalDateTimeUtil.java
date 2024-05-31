package com.livecard.front.common.web.formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    public String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public String formatDate(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return "-";
        }
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDate parseToLocalDate(String dateString, String pattern) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateString, formatter);
    }

}