package com.example.hackathon2024;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.hackathon2024.database.AppDatabase;
import com.example.hackathon2024.database.DailyReport;
import com.example.hackathon2024.database.DailyReportDao;
import com.example.hackathon2024.database.HealthRecord;
import com.example.hackathon2024.database.HealthRecordDao;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * 'Backup' data from daily_raw_data to daily_summary table
 */
public class BackupWorker extends Worker {

    private AppDatabase db;

    public BackupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db = AppDatabase.getInstance(context); // Access your Room database instance
    }

    @NonNull
    @Override
    public Result doWork() {
        // Run the backup data function
        backupData();
        return Result.success();
    }

    public void backupData() {
        DailyReportDao dailyReportDao = db.dailyReportDao();
        HealthRecordDao healthRecordDao = db.healthRecordDao();

        // Move data and clear the table
        CompletableFuture.supplyAsync(() -> {
            List<HealthRecord> reports = healthRecordDao.getAll();
            healthRecordDao.clear();
            return reports;
        }).thenAcceptAsync(reports -> {
            dailyReportDao.insert(DailyReport.create(reports));
        });
    }
}
