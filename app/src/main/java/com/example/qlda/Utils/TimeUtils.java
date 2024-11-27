package com.example.qlda.Utils;

import com.example.qlda.home.MyCustomLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String timeAgoLocalDate(String dateTime) {
        try {
            Date past = sdf.parse(dateTime);
            Date now = new Date();

            assert past != null;
            long duration = now.getTime() - past.getTime();

            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            long days = TimeUnit.MILLISECONDS.toDays(duration);

            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (minutes < 60) {
                return minutes + " minutes ago";
            } else if (hours < 24) {
                return hours + " hours ago";
            } else if (days < 30) {
                return days + " days ago";
            } else if (days < 365) {
                return (days / 30) + " months ago";
            } else {
                return (days / 365) + " years ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date format";
        }
    }

    public static LocalDate stringToLocalDate(String time) {
        return LocalDate.parse(time.split(" ")[0]);
    }
    public static String getCurrentTimeFormatted() {
        Date now = new Date();
        return sdf.format(now);
    }
}