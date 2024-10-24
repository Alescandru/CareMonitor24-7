package com.example.hackathon2024.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HealthRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HealthRecordDao healthRecordDao();

    private static AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "care_monitor").build();
        }
        return instance;
    }
}
