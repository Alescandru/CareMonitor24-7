package com.example.hackathon2024;

import android.os.Handler;

import java.util.Random;

class SensorSimulator {

    private final Random random = new Random();
    private final Handler handler = new Handler();
    private int pulse;
    private int oxygenLevel;
    private int systolicPressure;
    private int diastolicPressure;
    private boolean isRunning = false;

    private final int SIMULATION_INTERVAL = 2000; // 2 secunde

    public interface SensorDataListener {
        void onSensorDataChanged(int pulse, int oxygenLevel, int systolicPressure, int diastolicPressure);
    }

    private SensorDataListener listener;

    public SensorSimulator(SensorDataListener listener) {
        this.listener = listener;
    }

    public void startSimulation() {
        isRunning = true;
        simulateData();
    }

    public void stopSimulation() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void simulateData() {
        if (!isRunning) return;

        // Generăm date random pentru puls, oxigen și tensiune
        pulse = 60 + random.nextInt(40); // între 60 și 100 BPM
        oxygenLevel = 95 + random.nextInt(5); // între 95% și 100% SpO2
        systolicPressure = 110 + random.nextInt(20); // între 110 și 130 mmHg
        diastolicPressure = 70 + random.nextInt(10); // între 70 și 80 mmHg

        // Transmitem datele simulate către listener
        listener.onSensorDataChanged(pulse, oxygenLevel, systolicPressure, diastolicPressure);

        // Repetăm simularea la fiecare 2 secunde
        handler.postDelayed(this::simulateData, SIMULATION_INTERVAL);
    }
}
