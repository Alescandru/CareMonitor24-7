package com.example.hackathon2024;

import android.content.Context;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Used to schedule BackupWorker to do his job at midnight
 */
public class BackupScheduler {

    public static void scheduleBackup(Context context) {
        // Calculate the delay until the beginning of the next day (midnight)
        Calendar calendar = Calendar.getInstance();
        long currentTime = System.currentTimeMillis();

        // Set the calendar to the beginning of the next day (midnight)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long initialDelay = calendar.getTimeInMillis() - currentTime;

        // Schedule the worker to run every 24 hours
        PeriodicWorkRequest backupRequest = new PeriodicWorkRequest.Builder(BackupWorker.class,
                24, TimeUnit.HOURS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS) // Delay until midnight
                .build();

        // Enqueue the work
        WorkManager.getInstance(context).enqueue(backupRequest);
    }
}
