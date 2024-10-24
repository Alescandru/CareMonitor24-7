package com.example.hackathon2024.database;

import androidx.room.Database;

// TODO: Make it singleton
@Database(entities = {HealthRecord.class}, version = 1)
public abstract class AppDatabase {
    public abstract HealthRecordDao healthRecordDao();
}
