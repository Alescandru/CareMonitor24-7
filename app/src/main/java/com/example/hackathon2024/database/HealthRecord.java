package com.example.hackathon2024.database;

import static com.example.hackathon2024.utils.Utils.listAverage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.List;

// TODO: Change field visibility to private, add getters and setters

/**
 * Vital metrics record for a period of time
 */
@Entity(tableName = "daily_raw_data")
public class HealthRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "hear_beat_min")
    public int heartBeatMin;

    @ColumnInfo(name = "hear_beat_max")
    public int hearBeatMax;

    @ColumnInfo(name = "hear_beat_avg")
    public double hearBeatAvg;

    @ColumnInfo(name = "oxygen_level_min")
    public int oxygenLevelMin;

    @ColumnInfo(name = "oxygen_level_max")
    public int oxygenLevelMax;

    @ColumnInfo(name = "oxygen_level_avg")
    public double oxygenLevelAvg;

    @ColumnInfo(name = "systolic_pressure_max")
    public int systolicPressureMax;

    @ColumnInfo(name = "systolic_pressure_min")
    public int systolicPressureMin;

    @ColumnInfo(name = "systolic_pressure_avg")
    public double systolicPressureAvg;

    @ColumnInfo(name = "diastolic_pressure_max")
    public int diastolicPressureMax;

    @ColumnInfo(name = "diastolic_pressure_min")
    public int diastolicPressureMin;

    @ColumnInfo(name = "diastolic_pressure_avg")
    public double diastolicPressureAvg;

    public static HealthRecord create(List<Integer> heartBeatList, List<Integer> oxygenLevelList, List<Integer> systolicPressureList, List<Integer> diastolicPressureList){
        HealthRecord record = new HealthRecord();
        record.heartBeatMin = Collections.min(heartBeatList);
        record.hearBeatMax = Collections.max(heartBeatList);
        record.hearBeatAvg = listAverage(heartBeatList);

        record.oxygenLevelMin = Collections.min(oxygenLevelList);
        record.oxygenLevelMax = Collections.max(oxygenLevelList);
        record.oxygenLevelAvg = listAverage(oxygenLevelList);

        record.systolicPressureMin = Collections.min(systolicPressureList);
        record.systolicPressureMax = Collections.max(systolicPressureList);
        record.systolicPressureAvg = listAverage(systolicPressureList);

        record.diastolicPressureMin = Collections.min(diastolicPressureList);
        record.diastolicPressureMax = Collections.max(diastolicPressureList);
        record.diastolicPressureAvg = listAverage(diastolicPressureList);

        return record;
    }
}
