package com.example.hackathon2024.convertor;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {
    // Converts Long (timestamp) to Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    // Converts Date to Long (timestamp)
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    // Helper method to format Date as MM-dd
    public static String formatDateToMMDD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return date != null ? sdf.format(date) : null;
    }
}
