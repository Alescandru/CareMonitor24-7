package com.example.hackathon2024.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// TODO: Change field visibility to private, add getters and setters

@Entity(tableName = "health_records")
public class HealthRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "hear_beat")
    public int hearBeat;

    @ColumnInfo(name = "oxygen_level")
    public int oxygenLevel;

    @ColumnInfo(name = "systolic_pressure")
    public int systolicPressure;

    @ColumnInfo(name = "diastolic_pressure")
    public int diastolicPressure;
}
