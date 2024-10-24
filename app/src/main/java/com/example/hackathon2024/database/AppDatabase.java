package com.example.hackathon2024.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HealthRecord.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HealthRecordDao healthRecordDao();

    private static volatile AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (null == instance) {
            synchronized (AppDatabase.class) {
                if (null == instance) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "care_monitor").build();
                }
            }
        }
        return instance;
    }
}
