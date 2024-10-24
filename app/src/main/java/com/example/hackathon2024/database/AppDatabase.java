package com.example.hackathon2024.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.hackathon2024.convertor.Converters;

@Database(entities = {HealthRecord.class, DailyReport.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HealthRecordDao healthRecordDao();

    public abstract DailyReportDao dailyReportDao();

    private static volatile AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (null == instance) {
            synchronized (AppDatabase.class) {
                if (null == instance) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "care_monitor")
                            .fallbackToDestructiveMigration()   // TODO: Remove it
                            .build();
                }
            }
        }
        return instance;
    }
}
