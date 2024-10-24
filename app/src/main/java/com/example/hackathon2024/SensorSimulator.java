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
    private boolean isRunning = false; // Default value for isRunning

    private final int SIMULATION_INTERVAL = 2000; // 2 seconds

    public interface SensorDataListener {
        void onSensorDataChanged(int pulse, int oxygenLevel, int systolicPressure, int diastolicPressure);
    }

    private SensorDataListener listener;

    public SensorSimulator(SensorDataListener listener) {
        this.listener = listener;
    }

    public void startSimulation() {
        simulateData(); // Start simulating data
    }

    public void stopSimulation() {
        handler.removeCallbacksAndMessages(null); // Stop the handler from calling simulateData
    }

    // Method to set the isRunning state
    public void setRunning(boolean running) {
        this.isRunning = running; // Directly update the isRunning state
    }

    // Getter for isRunning
    public boolean isRunning() {
        return isRunning; // Return the current state of isRunning
    }

    private void simulateData() {
        // Check if the simulation is running and adjust thresholds accordingly
        if (isRunning) {
            // Higher thresholds when isRunning is true
            pulse = 100 + random.nextInt(51); // between 100 and 150 BPM
            oxygenLevel = 92 + random.nextInt(9); // between 92% and 100% SpO2
            systolicPressure = 160 + random.nextInt(41); // between 160 and 200 mmHg
            diastolicPressure = 80 + random.nextInt(31); // between 80 and 110 mmHg
        } else {
            // Normal thresholds when not running
            pulse = 60 + random.nextInt(40); // between 60 and 100 BPM
            oxygenLevel = 95 + random.nextInt(5); // between 95% and 100% SpO2
            systolicPressure = 110 + random.nextInt(20); // between 110 and 130 mmHg
            diastolicPressure = 70 + random.nextInt(10); // between 70 and 80 mmHg
        }

        // Introduce random health variations (1-10)
        int randomHealthFactor = random.nextInt(10) + 1; // Random value between 1 and 10

        if (randomHealthFactor == 10) {
            // Apply variations if randomHealthFactor is 10
            pulse += random.nextInt(11); // Increase pulse by 0 to 10 BPM
            oxygenLevel -= random.nextInt(5) + 1; // Decrease oxygen level by 1 to 5%
            systolicPressure += random.nextInt(31) + 10; // Increase systolic pressure by 10 to 40 mmHg
            diastolicPressure += random.nextInt(10) + 1; // Increase diastolic pressure by 1 to 10 mmHg
        }

        // Transmit simulated data to listener
        listener.onSensorDataChanged(pulse, oxygenLevel, systolicPressure, diastolicPressure);

        // Repeat simulation every 2 seconds
        handler.postDelayed(this::simulateData, SIMULATION_INTERVAL);
    }

}
