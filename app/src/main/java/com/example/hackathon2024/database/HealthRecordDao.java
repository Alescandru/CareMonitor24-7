package com.example.hackathon2024.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HealthRecordDao {

    @Insert
    void insertAll(HealthRecord ...records);
    @Delete
    void delete(HealthRecord record);

    @Query("SELECT * from daily_raw_data")
    List<HealthRecord> getAll();

    @Query("DELETE FROM daily_raw_data")
    void clear();
}
