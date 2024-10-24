package com.example.hackathon2024.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_records")
public class HealthRecord {
    @PrimaryKey
    public  int id;

    @ColumnInfo(name = "hear_beat")
    public int hearBeat;

    @ColumnInfo(name = "sistolic_pressure")
    public int sitolicPressure;

    @ColumnInfo(name = "diastolic_pressure")
    public int diastolicPressure;
}
