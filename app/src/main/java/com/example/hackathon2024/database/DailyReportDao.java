package com.example.hackathon2024.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DailyReportDao {
    @Insert
    void insert(DailyReport report);

    @Query("SELECT * from daily_summary")
    List<DailyReport> getAll();

    // TODO: Get this/last month reports
}
