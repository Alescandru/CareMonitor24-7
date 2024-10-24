package com.example.hackathon2024;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.hackathon2024.database.AppDatabase;


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
        }, AppDatabase.getInstance(this.getApplicationContext()));

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
