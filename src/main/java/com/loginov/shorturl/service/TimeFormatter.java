package com.loginov.shorturl.service;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeFormatter {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy - HH:mm:ss Z z");

    public String timeInUtc(ZonedDateTime time) {
        return time.format(dateTimeFormatter);
    }
}
