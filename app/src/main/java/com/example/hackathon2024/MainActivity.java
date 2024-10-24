package com.example.hackathon2024;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private TextView concluziiTextView;
    private TextView pulsTextView, oxigenTextView, tensiuneTextView;

    // Inițializare variabile pentru valori simulate
    private int puls;
    private int oxigen;
    private int tensiuneSistolic;
    private int tensiuneDiastolic;

    // Simulator de senzori
    private SensorSimulator simulator;
    Button buttonIsRunig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajustează padding-ul pentru layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inițializează elementele UI
        Button buttonAnalizare = findViewById(R.id.buttonAnalizare);
        concluziiTextView = findViewById(R.id.concluziiTextView);
        pulsTextView = findViewById(R.id.pulsVal);       // TextView pentru puls
        oxigenTextView = findViewById(R.id.oxigenVal);   // TextView pentru oxigen
        tensiuneTextView = findViewById(R.id.tensiuneVal); // TextView pentru tensiune

        // Creează simulatorul de senzori și pornește simularea
        simulator = new SensorSimulator(new SensorSimulator.SensorDataListener() {
            @Override
            public void onSensorDataChanged(int pulse, int oxygenLevel, int systolicPressure, int diastolicPressure) {
                // Actualizează valorile din interfață
                puls = pulse;
                oxigen = oxygenLevel;
                tensiuneSistolic = systolicPressure;
                tensiuneDiastolic = diastolicPressure;

                pulsTextView.setText(pulse + " BPM");
                oxigenTextView.setText(oxygenLevel + "% SpO2");
                tensiuneTextView.setText(systolicPressure + "/" + diastolicPressure + " mmHg");
            }
        });

        // Pornește simularea la inițializarea aplicației
        simulator.startSimulation();

        // Setează listener-ul pentru butonul de analizare
        buttonAnalizare.setOnClickListener(view -> analizaDateMedicale());

        buttonIsRunig = findViewById(R.id.isRunningB);
        buttonIsRunig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the isRunning state
                boolean newState = !simulator.isRunning(); // Use the getter method
                simulator.setRunning(newState); // Update the simulator's running state

                // Optionally, update the button text based on the new state
                buttonIsRunig.setText(newState ? "Running" : "notRunning");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Oprește simularea la distrugerea activității
        simulator.stopSimulation();
    }

    // Funcția care analizează datele medicale
    private void analizaDateMedicale() {
        StringBuilder concluzii = new StringBuilder();

        // Check if simulation is running and set thresholds accordingly
        boolean running = simulator.isRunning(); // Get the current running state

        // Analiza pulsului
        if (running) {
            // Higher thresholds for running state
            if (puls < 100) {
                concluzii.append("Bradicardie: Pulsul este prea mic.\n");
            } else if (puls > 150) {
                concluzii.append("Tahicardie: Pulsul este prea mare.\n");
            } else {
                concluzii.append("Pulsul este în limite normale.\n");
            }
        } else {
            // Normal thresholds when not running
            if (puls < 60) {
                concluzii.append("Bradicardie: Pulsul este prea mic.\n");
            } else if (puls > 100) {
                concluzii.append("Tahicardie: Pulsul este prea mare.\n");
            } else {
                concluzii.append("Pulsul este în limite normale.\n");
            }
        }

        // Analiza nivelului de oxigen din sânge
        if (running) {
            // Adjusted threshold for oxygen level when running
            if (oxigen < 92) {
                concluzii.append("Nivel scăzut de oxigen: Hipoxemie.\n");
            } else {
                concluzii.append("Nivelul de oxigen este în limite normale.\n");
            }
        } else {
            // Normal threshold for oxygen level
            if (oxigen < 90) {
                concluzii.append("Nivel scăzut de oxigen: Hipoxemie.\n");
            } else {
                concluzii.append("Nivelul de oxigen este în limite normale.\n");
            }
        }

        // Analiza tensiunii arteriale
        if (running) {
            // Higher thresholds for blood pressure when running
            if (tensiuneSistolic > 200 || tensiuneDiastolic > 110) {
                concluzii.append("Tensiune arterială ridicată: Hipertensiune.\n");
            } else if (tensiuneSistolic < 160 || tensiuneDiastolic < 80) {
                concluzii.append("Tensiune arterială scăzută: Hipotensiune.\n");
            } else {
                concluzii.append("Tensiunea arterială este în limite normale.\n");
            }
        } else {
            // Normal thresholds for blood pressure when not running
            if (tensiuneSistolic > 140 || tensiuneDiastolic > 90) {
                concluzii.append("Tensiune arterială ridicată: Hipertensiune.\n");
            } else if (tensiuneSistolic < 90 || tensiuneDiastolic < 60) {
                concluzii.append("Tensiune arterială scăzută: Hipotensiune.\n");
            } else {
                concluzii.append("Tensiunea arterială este în limite normale.\n");
            }
        }

        // Afișează concluziile în TextView
        concluziiTextView.setText(concluzii.toString());
    }

}
