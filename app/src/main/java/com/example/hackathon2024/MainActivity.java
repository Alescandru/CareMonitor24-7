package com.example.hackathon2024;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Analiza pulsului
        if (puls < 60) {
            concluzii.append("Bradicardie: Pulsul este prea mic.\n");
        } else if (puls > 100) {
            concluzii.append("Tahicardie: Pulsul este prea mare.\n");
        } else {
            concluzii.append("Pulsul este în limite normale.\n");
        }

        // Analiza nivelului de oxigen din sânge
        if (oxigen < 90) {
            concluzii.append("Nivel scăzut de oxigen: Hipoxemie.\n");
        } else {
            concluzii.append("Nivelul de oxigen este în limite normale.\n");
        }

        // Analiza tensiunii arteriale
        if (tensiuneSistolic > 140 || tensiuneDiastolic > 90) {
            concluzii.append("Tensiune arterială ridicată: Hipertensiune.\n");
        } else if (tensiuneSistolic < 90 || tensiuneDiastolic < 60) {
            concluzii.append("Tensiune arterială scăzută: Hipotensiune.\n");
        } else {
            concluzii.append("Tensiunea arterială este în limite normale.\n");
        }

        // Afișează concluziile în TextView
        concluziiTextView.setText(concluzii.toString());
    }
}
