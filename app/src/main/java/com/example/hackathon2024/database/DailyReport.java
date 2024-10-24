package com.example.hackathon2024.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity(tableName = "daily_summary")
public class DailyReport {
    // TODO: Add type convertor for date
    @PrimaryKey
    public Date date;

    @ColumnInfo(name = "hear_beat_min")
    public int heartBeatMin = 0;

    @ColumnInfo(name = "hear_beat_max")
    public int hearBeatMax = 0;

    @ColumnInfo(name = "hear_beat_avg")
    public int hearBeatAvg = 0;

    @ColumnInfo(name = "oxygen_level_min")
    public int oxygenLevelMin = 0;

    @ColumnInfo(name = "oxygen_level_max")
    public int oxygenLevelMax = 0;

    @ColumnInfo(name = "oxygen_level_avg")
    public int oxygenLevelAvg = 0;

    @ColumnInfo(name = "systolic_pressure_max")
    public int systolicPressureMax = 0;

    @ColumnInfo(name = "systolic_pressure_min")
    public int systolicPressureMin = 0;

    @ColumnInfo(name = "systolic_pressure_avg")
    public int systolicPressureAvg = 0;

    @ColumnInfo(name = "diastolic_pressure_max")
    public int diastolicPressureMax = 0;

    @ColumnInfo(name = "diastolic_pressure_min")
    public int diastolicPressureMin = 0;

    @ColumnInfo(name = "diastolic_pressure_avg")
    public int diastolicPressureAvg = 0;

    public void addHealthRecord(HealthRecord record) {
        this.heartBeatMin += record.heartBeatMin;
        this.hearBeatMax += record.hearBeatMax;
        this.hearBeatAvg += (int) record.hearBeatAvg;

        this.oxygenLevelMin += record.oxygenLevelMin;
        this.oxygenLevelMax += record.oxygenLevelMax;
        this.oxygenLevelAvg += (int) record.oxygenLevelAvg;

        this.systolicPressureMin += record.systolicPressureMin;
        this.systolicPressureMax += record.systolicPressureMax;
        this.systolicPressureAvg += (int) record.systolicPressureAvg;

        this.diastolicPressureMin += record.diastolicPressureMin;
        this.diastolicPressureMax += record.diastolicPressureMax;
        this.diastolicPressureAvg += (int) record.diastolicPressureAvg;
    }

    /**
     * Create a DailyReport from a list of HealthReports
     *
     * @param records a list of health records
     * @return a newly created report
     */
    public static DailyReport create(List<HealthRecord> records) {
        DailyReport report = new DailyReport();

        report.date = new Date();

        for (HealthRecord record : records) {
            report.addHealthRecord(record);
        }

        int count = records.size();
        report.heartBeatMin = report.heartBeatMin / count;
        report.hearBeatMax = report.hearBeatMax / count;
        report.hearBeatAvg = report.hearBeatAvg / count;
        report.oxygenLevelMin = report.oxygenLevelMin / count;
        report.oxygenLevelMax = report.oxygenLevelMax / count;
        report.oxygenLevelAvg = report.oxygenLevelAvg / count;
        report.systolicPressureMin = report.systolicPressureMin / count;
        report.systolicPressureMax = report.systolicPressureMax / count;
        report.systolicPressureAvg = report.systolicPressureAvg / count;
        report.diastolicPressureMin = report.diastolicPressureMin / count;
        report.diastolicPressureMax = report.diastolicPressureMax / count;
        report.diastolicPressureAvg = report.diastolicPressureAvg / count;

        return report;
    }

}
