package com.example.qlda.Utils;

import com.example.qlda.home.MyCustomLog;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime stringToDate(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }

    public static String timeAgo(String commentTimeString) {
        LocalDateTime commentTime = stringToDate(commentTimeString);
        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(commentTime, now).getSeconds();

        if (seconds < 60) {
            return "Vừa mới đây";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 2592000) {
            long days = seconds / 86400;
            return days + " ngày trước";
        } else if (seconds < 31536000) {
            long months = seconds / 2592000;
            return months + " tháng trước";
        } else {
            long years = seconds / 31536000;
            return years + " năm trước";
        }
    }

    public static String timeAgoDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(date, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return (seconds == 1) ? "1 giây trước" : seconds + " giây trước";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return (minutes == 1) ? "1 phút trước" : minutes + " phút trước";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return (hours == 1) ? "1 giờ trước" : hours + " giờ trước";
        }

        long days = hours / 24;
        if (days < 30) {
            return (days == 1) ? "1 ngày trước" : days + " ngày trước";
        }

        long months = days / 30;
        if (months < 12) {
            return (months == 1) ? "1 tháng trước" : months + " tháng trước";
        }

        long years = days / 365;
        return (years == 1) ? "1 năm trước" : years + " năm trước";
    }

    public static LocalDateTime stringToLocalDateTime(String dateTimeString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static String localDateTimeToString(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);

//        String dateTimeString = "2024-11-23 14:30:00";
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
//
//        System.out.println("LocalDateTime: " + localDateTime);
//
//        MyCustomLog.DebugLog("LocalDateTime"," " + localDateTime);
//
////        LocalDateTime now = LocalDateTime.now();
//        String formattedDate = localDateTime.format(formatter);
//
//        System.out.println("Formatted DateTime: " + formattedDate);
//
//        MyCustomLog.DebugLog("LocalDateTime"," " + formattedDate);
    }
}